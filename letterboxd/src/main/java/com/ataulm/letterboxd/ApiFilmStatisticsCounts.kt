package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilmStatisticsCounts(
    @field:Json(name = "watches") val watches: Int,
    @field:Json(name = "likes") val likes: Int,
    @field:Json(name = "ratings") val ratings: Int
)
