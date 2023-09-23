package com.ataulm.letterboxd.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiUserTokens(
    @field:Json(name = "access_token") val accessToken: String,
    @field:Json(name = "refresh_token") val refreshToken: String
)
