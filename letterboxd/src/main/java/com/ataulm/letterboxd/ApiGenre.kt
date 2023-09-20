package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGenre(
    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "name")
    val name: String
)
