package com.ataulm.letterboxd

import retrofit2.http.*

interface LetterboxdApi {

    @GET("films")
    suspend fun popularFilmsThisWeek(
        @Query("cursor") cursor: String?,
        @Query("perPage") perPage: Int,
        @Query("sort") sort: String = "FilmPopularityThisWeek"
    ): ApiPopularFilmsThisWeekResponse

    @RequiresAuthenticatedUser
    @GET("me")
    suspend fun me(): ApiMeResponse

    @GET("search")
    suspend fun search(@Query("input") searchTerm: String): ApiSearchResponse

    @GET("film/{id}")
    suspend fun film(@Path("id") letterboxdId: String): ApiFilm

    @GET("film/{id}/statistics")
    suspend fun filmStats(@Path("id") letterboxdId: String): ApiFilmStatistics

    @RequiresAuthenticatedUser
    @GET("member/{id}/watchlist")
    suspend fun watchList(
        @Path("id") memberId: String,
        @Query("cursor") cursor: String?,
        @Query("perPage") perPage: Int,
        @Query("sort") sort: String = "Added" // sort by most-recently added first
    ): ApiWatchListResponse

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

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuthenticatedUser
