package com.ataulm.whatsnext.api.auth

import com.ataulm.whatsnext.BuildConfig
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Functions to obtain/refresh user and client tokens, which must be used for all API calls
 * (except these).
 */
interface LetterboxdAuthApi {

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun fetchUserAuthTokens(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = "content:modify",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): AuthTokenApiResponse

    @FormUrlEncoded
    @POST("auth/token")
    fun fetchAnonymousAuthToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): Call<AnonymousAuthTokenApiResponse>

    @FormUrlEncoded
    @POST("auth/token")
    fun refreshAuthTokens(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): Call<AuthTokenApiResponse>
}

data class AuthTokenApiResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

data class AnonymousAuthTokenApiResponse(
    @SerializedName("access_token") val accessToken: String
)
