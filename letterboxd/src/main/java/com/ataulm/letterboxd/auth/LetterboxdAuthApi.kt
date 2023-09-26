package com.ataulm.letterboxd.auth

import com.ataulm.letterboxd.BuildConfig
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Functions to obtain/refresh user and client tokens, which must be used for all API calls
 * (except these).
 */
internal interface LetterboxdAuthApi {

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun fetchUserTokens(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = "content:modify",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): ApiUserTokens

    @FormUrlEncoded
    @POST("auth/token")
    fun fetchClientAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): Call<ApiClientAccessToken>

    @FormUrlEncoded
    @POST("auth/token")
    fun refreshUserTokens(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = BuildConfig.LETTERBOXD_KEY,
        @Field("client_secret") clientSecret: String = BuildConfig.LETTERBOXD_SECRET
    ): Call<ApiUserTokens>
}
