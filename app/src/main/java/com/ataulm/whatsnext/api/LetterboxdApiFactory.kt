package com.ataulm.whatsnext.api

import com.ataulm.support.Clock
import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.TokensStore
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

private const val LETTERBOXD_BASE_URL = "https://api.letterboxd.com/api/v0/"

class LetterboxdApiFactory(
        private val apiKey: String,
        private val apiSecret: String,
        private val tokensStore: TokensStore,
        private val clock: Clock,
        private val enableHttpLogging: Boolean
) {

    fun createRemote(): LetterboxdApi {
        val apiKeyIntercepter = AddApiKeyQueryParameterInterceptor(apiKey, clock)

        val refreshAccessTokenClient = OkHttpClient.Builder().apply {
            addNetworkInterceptor(apiKeyIntercepter)
            if (enableHttpLogging) {
                val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(httpLoggingInterceptor)
            }
        }.build()

        val refreshAccessTokenApi = Retrofit.Builder()
                .client(refreshAccessTokenClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LETTERBOXD_BASE_URL)
                .build()
                .create(LetterboxdRefreshAccessTokenApi::class.java)

        val okHttpClient = OkHttpClient.Builder().apply {
            addNetworkInterceptor(apiKeyIntercepter)
            addNetworkInterceptor(AddAuthorizationHeaderInterceptor(apiSecret, tokensStore))
            addNetworkInterceptor(RefreshAccessTokenInterceptor(refreshAccessTokenApi, tokensStore))
            if (enableHttpLogging) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }
        }.build()

        return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LETTERBOXD_BASE_URL)
                .build()
                .create(LetterboxdApi::class.java)
    }
}

private class AddApiKeyQueryParameterInterceptor(private val apiKey: String, private val clock: Clock) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("nonce", UUID.randomUUID().toString())
                .addQueryParameter("timestamp", TimeUnit.MILLISECONDS.toSeconds(clock.currentTimeMillis).toString())
                .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}

private const val HEADER_AUTH = "Authorization"

private class AddAuthorizationHeaderInterceptor(private val apiSecret: String, private val tokensStore: TokensStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val originalUrl = chain.request().url

        val signature = generateSignature(chain.request().method, originalUrl, chain.request().body)
        if (chain.request().requiresAuthenticatedUser()) {
            val urlWithSignature = originalUrl.newBuilder().addQueryParameter("signature", signature).build()
            builder.url(urlWithSignature)
        } else {
            builder.url(originalUrl)
        }

        if (chain.request().requiresAuthenticatedUser()) {
            builder.addHeader(HEADER_AUTH, "Bearer ${requireNotNull(tokensStore.token).accessToken}")
        } else {
            builder.addHeader(HEADER_AUTH, "Signature $signature")
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
        val preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s",
                httpMethod.toUpperCase(Locale.US), url, urlEncodedBody)
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US)
    }

    private fun FormBody.toUrlEncodedString(): String {
        val pairs = ArrayList<String>()
        for (i in 0 until size) {
            pairs.add(encodedName(i) + "=" + encodedValue(i))
        }
        return pairs.joinToString(separator = "&")
    }
}

private class RefreshAccessTokenInterceptor(
        private val refreshAccessTokenApi: LetterboxdRefreshAccessTokenApi,
        private val tokensStore: TokensStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val token = tokensStore.token
        if (response.code != 401 || token == null || !chain.request().requiresAuthenticatedUser()) {
            // 1) we only care about unauthorized
            // 2) if token is null, user needs to sign in explicitly
            // 3) if this request doesn't need access token, let's not get one
            return response
        }

        val updatedToken = refreshAccessTokenApi.refreshAuthToken(token.refreshToken)
                .let { Token(it.accessToken, it.refreshToken, it.secondsUntilExpiry) }
        tokensStore.store(updatedToken)
        return chain.proceed(chain.request())
    }
}

private fun Request.requiresAuthenticatedUser(): Boolean {
    return tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(RequiresAuthenticatedUser::class.java) != null
}
