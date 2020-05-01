package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.*

/**
 * Retrofit implementation of [Letterboxd]. When that's been removed, this comment can disappear.
 */
interface LetterboxdApi {

    @FormUrlEncoded
    @POST("auth/token")
    fun fetchAuthToken(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grantType: String = "password"
    ): Single<AuthTokenApiResponse>

    @FormUrlEncoded
    @POST("auth/token")
    fun refreshAuthToken(
            @Field("refresh_token") refreshToken: String,
            @Field("grant_type") grantType: String = "refresh_token"
    ): Single<AuthTokenApiResponse>

    @GET("search")
    fun search(@Query("input") searchTerm: String): Single<ApiSearchResponse>
}

data class AuthTokenApiResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("expires_in") val secondsUntilExpiry: Long,
        @SerializedName("refresh_token") val refreshToken: String
)
