package com.ataulm.whatsnext.letterboxd

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Letterboxd {

    @FormUrlEncoded
    @POST("/auth/token")
    fun accessToken(@Field("username") username: String,
                    @Field("password") password: String,
                    @Field("grant_type") grantType: String = "password"): Observable<Token>

    data class Token(val access_token: String, val refresh_token: String, val expires_in: Long)
}
