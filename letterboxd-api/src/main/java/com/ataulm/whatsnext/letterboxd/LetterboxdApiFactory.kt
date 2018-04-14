package com.ataulm.whatsnext.letterboxd

import com.ataulm.support.Clock2
import okhttp3.FormBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class LetterboxdApiFactory(private val apiKey: String, private val apiSecret: String, private val clock2: Clock2) {

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
                            .addQueryParameter("apikey", apiKey)
                            .addQueryParameter("nonce", UUID.randomUUID().toString())
                            .addQueryParameter("timestamp", TimeUnit.MILLISECONDS.toSeconds(clock2.currentTimeMillis).toString())
                            .build()
                    builder.url(url)
                    val urlEncodedBody = (chain.request().body() as FormBody).toUrlEncodedString()
                    builder.addHeader("Authorization", "Signature ${generateSignature(apiSecret, chain.request().method(), url.toString(), urlEncodedBody)}")
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

    private fun generateSignature(apiSecret: String, httpMethod: String, url: String, urlEncodedBody: String): String {
        val preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", httpMethod.toUpperCase(Locale.US), url, urlEncodedBody)
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US)
    }
}

private fun FormBody.toUrlEncodedString(): String {
    val pairs = ArrayList<String>()
    for (i in 0 until size()) {
        pairs.add(encodedName(i) + "=" + encodedValue(i))
    }
    return pairs.joinToString(separator = "&")
}
