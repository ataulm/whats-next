package com.ataulm.letterboxd.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClientAccessTokenApiResponse(
    @field:Json(name = "access_token") val accessToken: String
)
