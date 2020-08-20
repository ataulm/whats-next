package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuthenticatedUser

interface LetterboxdApi {

    @GET("films")
    suspend fun popularFilmsThisWeek(
            @Query("cursor") cursor: String?,
            @Query("perPage") perPage: Int,
            @Query("sort") sort: String = "FilmPopularityThisWeek"
    ): ApiPopularFilmsThisWeekResponse

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

    @GET("film/{id}/statistics")
    suspend fun filmStats(@Path("id") letterboxdId: String): ApiFilmStatistics

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

data class ApiPopularFilmsThisWeekResponse(
        @SerializedName("next") val cursor: String? = null,
        @SerializedName("items") val items: List<ApiFilmSummary>
)

data class ApiFilmStatistics(
        @SerializedName("rating") val rating: Float? = null,
        @SerializedName("counts") val counts: ApiFilmStatisticsCounts,
        @SerializedName("ratingsHistogram") val ratingsHistogram: List<ApiRatingsHistogramBar>
)

data class ApiFilmStatisticsCounts(
        @SerializedName("watches") val watches: Int,
        @SerializedName("likes") val likes: Int
)

data class ApiRatingsHistogramBar(
        @SerializedName("rating") val rating: Float,
        @SerializedName("normalizedWeight") val weight: Float,
        @SerializedName("count") val count: Int
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
        @SerializedName("data") val data: ApiFilmRelationship
)
