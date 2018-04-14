package com.ataulm.whatsnext.letterboxd

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LetterboxdApi {

    @FormUrlEncoded
    @POST("/auth/token")
    fun accessToken(@Field("username") username: String,
                    @Field("password") password: String,
                    @Field("grant_type") grantType: String = "password"): Observable<AccessToken>

    data class AccessToken(val access_token: String, val refresh_token: String, val expires_in: Long)
}
