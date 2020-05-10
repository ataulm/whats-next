package com.ataulm.whatsnext.api

import com.ataulm.support.Clock
import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.TokensStore
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
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
        val gsonConverterFactory = GsonConverterFactory.create()

        val refreshAccessTokenApi = Retrofit.Builder()
                .client(OkHttpClient.Builder().addCommonNetworkInterceptors().build())
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(LETTERBOXD_BASE_URL)
                .build()
                .create(RefreshAccessTokenApi::class.java)

        return Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        // it's important that this one is set first, since it relies on the other to set the Auth header when a request is retried (after refreshing access token)
                        .addInterceptor(RefreshAccessTokenInterceptor(refreshAccessTokenApi, tokensStore))
                        .addCommonNetworkInterceptors()
                        .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .baseUrl(LETTERBOXD_BASE_URL)
                .build()
                .create(LetterboxdApi::class.java)
    }

    private fun OkHttpClient.Builder.addCommonNetworkInterceptors(): OkHttpClient.Builder {
        addNetworkInterceptor(AddApiKeyQueryParameterInterceptor(apiKey, clock))
        addNetworkInterceptor(AddAuthorizationHeaderInterceptor(apiSecret, tokensStore))
        if (enableHttpLogging) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addNetworkInterceptor(httpLoggingInterceptor)
        }
        return this
    }
}

/**
 * If API key is invalid (or other parts of the signature) -> 401 {"message":"Signature invalid","type":"signature"}
 */
private class AddApiKeyQueryParameterInterceptor(private val apiKey: String, private val clock: Clock) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("nonce", UUID.randomUUID().toString())
                .addQueryParameter("timestamp", TimeUnit.MILLISECONDS.toSeconds(clock.currentTimeMillis).toString())
                .build()
        val amendedRequest = chain.request().newBuilder().url(url).build()
        return chain.proceed(amendedRequest)
    }
}

private const val HEADER_AUTH = "Authorization"

private class AddAuthorizationHeaderInterceptor(private val apiSecret: String, private val tokensStore: TokensStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val amendedRequest = chain.request().newBuilder()
        val signature = chain.request().generateHashedSignature()

        if (chain.request().requiresAuthenticatedUser()) {
            amendedRequest.addHeader(HEADER_AUTH, "Bearer ${requireNotNull(tokensStore.token).accessToken}")
            val url = chain.request().url.newBuilder()
                    .addQueryParameter("signature", signature)
                    .build()
            amendedRequest.url(url)
        } else {
            amendedRequest.addHeader(HEADER_AUTH, "Signature $signature")
        }

        return chain.proceed(amendedRequest.build())
    }

    private fun Request.generateHashedSignature(): String {
        val urlEncodedBody = (body as? FormBody)?.toUrlEncodedString() ?: ""
        val preHashedSignature = "${method.toUpperCase(Locale.US)}\u0000$url\u0000$urlEncodedBody"
        return HmacSha256.generateHash(apiSecret, preHashedSignature).toLowerCase(Locale.US)
    }

    private fun FormBody.toUrlEncodedString(): String {
        val pairs = ArrayList<String>()
        for (i in 0 until size) {
            pairs.add(encodedName(i) + "=" + encodedValue(i))
        }
        return pairs.joinToString(separator = "&")
    }
}

/**
 * Fetches/stores new access token if:
 * - response was unauthorized (401)
 * - && user is signed in (token store is not null)
 * - && request requires an authenticated user
 */
private class RefreshAccessTokenInterceptor(
        private val refreshAccessTokenApi: RefreshAccessTokenApi,
        private val tokensStore: TokensStore
) : Interceptor {

    private var alreadyRetriedRequest = false

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val token = tokensStore.token
        if (alreadyRetriedRequest || response.code != 401 || token == null || !chain.request().requiresAuthenticatedUser()) {
            return response
        }

        val updatedToken = refreshAccessToken(token.refreshToken) ?: return response
        tokensStore.store(updatedToken)

        // We only want to try this once per original request, since there's other reasons we could get a 401 (e.g. signature is not valid)
        alreadyRetriedRequest = true
        val retriedResponse = chain.proceed(chain.request())
        if (retriedResponse.isSuccessful) {
            // reset for the next request
            alreadyRetriedRequest = false
        }

        return retriedResponse
    }

    private fun refreshAccessToken(refreshToken: String): Token? {
        return refreshAccessTokenApi.refreshAuthToken(refreshToken).execute()
                .let { response ->
                    response.body()?.let {
                        Token(it.accessToken, it.refreshToken)
                    }
                }
    }
}

private fun Request.requiresAuthenticatedUser(): Boolean {
    return tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(RequiresAuthenticatedUser::class.java) != null
}

private interface RefreshAccessTokenApi {

    @FormUrlEncoded
    @POST("auth/token")
    fun refreshAuthToken(
            @Field("refresh_token") refreshToken: String,
            @Field("grant_type") grantType: String = "refresh_token"
    ): Call<AuthTokenApiResponse>
}
