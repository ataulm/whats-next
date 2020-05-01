package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}

data class AuthTokenApiResponse(
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("expires_in") val secondsUntilExpiry: Long,
        @SerializedName("refresh_token") val refreshToken: String
)
