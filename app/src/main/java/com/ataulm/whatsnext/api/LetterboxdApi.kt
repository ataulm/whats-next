package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuthenticatedUser

interface LetterboxdApi {

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun fetchAuthToken(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("grant_type") grantType: String = "password"
    ): AuthTokenApiResponse

    @GET("search")
    suspend fun search(@Query("input") searchTerm: String): ApiSearchResponse

    @GET("film/{id}")
    suspend fun film(@Path("id") letterboxdId: String): ApiFilm

    @RequiresAuthenticatedUser
    @GET("film/{id}/me")
    suspend fun filmRelationship(@Path("id") letterboxdId: String): ApiFilmRelationship
}

data class AuthTokenApiResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("refresh_token") val refreshToken: String
)
