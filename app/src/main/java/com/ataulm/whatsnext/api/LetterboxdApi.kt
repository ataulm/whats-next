package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    fun film(@Path("id") letterboxdId: String): Single<ApiFilm>

    @RequiresAuthenticatedUser
    @GET("film/{id}/me")
    fun filmRelationship(@Path("id") letterboxdId: String): Single<ApiFilmRelationship>
}

data class AuthTokenApiResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("refresh_token") val refreshToken: String
)
