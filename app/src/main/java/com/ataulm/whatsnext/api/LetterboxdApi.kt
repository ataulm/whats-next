package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.*

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuthenticatedUser

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

    @GET("film/{id}")
    fun film(@Path("id") letterboxdId: String): Single<ApiFilm>

    @RequiresAuthenticatedUser
    @GET("film/{id}/me")
    fun filmRelationship(@Path("id") letterboxdId: String): Single<ApiFilmRelationship>
}

data class AuthTokenApiResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("expires_in") val secondsUntilExpiry: Long,
        @SerializedName("refresh_token") val refreshToken: String
)
