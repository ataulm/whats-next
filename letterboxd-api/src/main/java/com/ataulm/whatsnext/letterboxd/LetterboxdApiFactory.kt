package com.ataulm.whatsnext.letterboxd

import com.ataulm.support.Clock
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class LetterboxdApiFactory(private val apiKey: String, private val apiSecret: String, private val clock: Clock) {

    fun create(mockResponses: Boolean = false): LetterboxdApi {
        return if (mockResponses) {
            MockResponsesLetterboxdApi()
        } else {
            remoteLetterboxdApi()
        }
    }

    private fun remoteLetterboxdApi(): LetterboxdApi {
        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                    val url = chain.request().url().newBuilder()
                            .addQueryParameter("api_key", apiKey)
                            .addQueryParameter("nonce", UUID.randomUUID().toString())
                            .addQueryParameter("timestamp", TimeUnit.MILLISECONDS.toSeconds(clock.currentTimeMillis).toString())
                            .build()
                    builder.url(url)
                    builder.addHeader("accept", "application/json") // TODO: might not be necessary
                    builder.addHeader("Authorization", "Signature ${generateSignature(chain.request().method(), url.toString(), chain.request().body().toString(), apiSecret)}")
                    chain.proceed(builder.build())
                }
                .build()

        return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.letterboxd.com/api/v0/")
                .build()
                .create(LetterboxdApi::class.java)
    }

    private fun generateSignature(httpMethod: String, url: String, body: String, apiSecret: String): String {
        val preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", httpMethod.toUpperCase(Locale.US), url, body)
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US)
    }
}
