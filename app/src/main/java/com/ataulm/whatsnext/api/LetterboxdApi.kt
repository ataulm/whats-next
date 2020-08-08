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

    @RequiresAuthenticatedUser
    @PATCH("film/{id}/me")
    suspend fun updateFilmRelationship(
            @Path("id") letterboxdId: String,
            @Body request: ApiFilmRelationshipUpdateRequest
    ): ApiFilmRelationshipUpdateResponse
}

data class AuthTokenApiResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("refresh_token") val refreshToken: String
)

data class ApiFilmRelationshipUpdateRequest(
        @SerializedName("watched") val watched: Boolean,
        @SerializedName("liked") val liked: Boolean,
        @SerializedName("inWatchlist") val inWatchlist: Boolean,
        /**
         * Accepts values between 0.5 and 5.0, with increments of 0.5, or null (to remove the
         * rating). If set, [watched] is assumed to be true.
         */
        @SerializedName("rating") val rating: String
)

data class ApiFilmRelationshipUpdateResponse(
        @SerializedName("data") val data: ApiFilmRelationship,
        @SerializedName("messages") val messages: List<ApiFilmRelationshipUpdateMessage>
)

data class ApiFilmRelationshipUpdateMessage(
        /**
         * {Error, Success}
         */
        @SerializedName("type") val type: String,
        /**
         * {InvalidRatingValue, UnableToRemoveWatch}
         */
        @SerializedName("code") val code: String,
        /**
         * The error message text in human-readable form.
         */
        @SerializedName("title") val title: String
)
