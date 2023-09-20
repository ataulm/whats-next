package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiLink(
    @field:Json(name = "type")
    val type: String,

    @field:Json(name = "id")
    val id: String
)
