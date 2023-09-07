package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.BuildConfig
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LetterboxdAuthApi {

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun fetchAnonymousAuthToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): AnonymousAuthTokenApiResponse

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun fetchAuthTokens(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = "content:modify",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): AuthTokenApiResponse

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun refreshAuthTokens(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): AuthTokenApiResponse
}

data class AuthTokenApiResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

data class AnonymousAuthTokenApiResponse(
    @SerializedName("access_token") val accessToken: String
)
