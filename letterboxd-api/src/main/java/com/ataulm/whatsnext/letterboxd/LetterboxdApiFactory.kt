package com.ataulm.whatsnext.letterboxd

import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class LetterboxdApiFactory(private val tokenStore: TokenStore, private val apiKey: String, private val apiSecret: String, private val clock: Clock) {

    fun create(mockResponses: Boolean = false): LetterboxdApi {
        return if (mockResponses) {
            MockResponsesLetterboxdApi()
        } else {
            remoteLetterboxdApi()
        }
    }

    private fun remoteLetterboxdApi(): LetterboxdApi {
        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(AddApiKeyQueryParameterInterceptor(apiKey, clock))
                .addNetworkInterceptor(AddAuthorizationHeaderInterceptor(apiSecret, tokenStore))
                .build()

        val gson = GsonBuilder()
                .registerTypeAdapter(SearchResponse.AbstractSearchItem::class.java, AbstractSearchItemDeserializer())
                .create()

        return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://api.letterboxd.com/api/v0/")
                .build()
                .create(LetterboxdApi::class.java)
    }


}

private class AddApiKeyQueryParameterInterceptor(private val apiKey: String, private val clock: Clock) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("nonce", UUID.randomUUID().toString())
                .addQueryParameter("timestamp", TimeUnit.MILLISECONDS.toSeconds(clock.currentTimeMillis()).toString())
                .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}

private class AddAuthorizationHeaderInterceptor(private val apiSecret: String, private val tokenStore: TokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val url = chain.request().url()

        val signature = generateSignature(chain.request().method(), url, chain.request().body())
        if (url.encodedPath().contains("auth/token")) { // TODO: worth enum/constantizing this so it's referenced from one place?
            builder.url(url)
            builder.addHeader("Authorization", "Signature $signature")
        } else {
            builder.url(url.newBuilder().addQueryParameter("signature", signature).build())
            // TODO: if the tokenStore is empty/token is invalid - the API call will fail
            builder.addHeader("Authorization", "Bearer ${tokenStore.token()?.access_token}")
        }
        return chain.proceed(builder.build())
    }

    private fun generateSignature(httpMethod: String, url: HttpUrl, requestBody: RequestBody?): String {
        return if (requestBody == null) {
            generateSignature(httpMethod, url.toString(), "")
        } else {
            generateSignature(httpMethod, url.toString(), (requestBody as FormBody).toUrlEncodedString())
        }
    }

    private fun generateSignature(httpMethod: String, url: String, urlEncodedBody: String): String {
        val preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", httpMethod.toUpperCase(Locale.US), url, urlEncodedBody)
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US)
    }

    private fun FormBody.toUrlEncodedString(): String {
        val pairs = ArrayList<String>()
        for (i in 0 until size()) {
            pairs.add(encodedName(i) + "=" + encodedValue(i))
        }
        return pairs.joinToString(separator = "&")
    }
}
