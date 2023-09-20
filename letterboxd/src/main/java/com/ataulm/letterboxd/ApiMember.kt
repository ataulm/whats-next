package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiMember(
    @field:Json(name = "id") val letterboxdId: String,
    @field:Json(name = "avatar") val avatar: ApiImage
)
