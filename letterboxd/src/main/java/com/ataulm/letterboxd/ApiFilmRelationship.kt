package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilmRelationship(
    @field:Json(name = "watched")
    val watched: Boolean,

    @field:Json(name = "liked")
    val liked: Boolean,

    @field:Json(name = "inWatchlist")
    val inWatchlist: Boolean,

    @field:Json(name = "rating")
    val rating: Float?
)
