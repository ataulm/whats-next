package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPopularFilmsThisWeekResponse(
    @field:Json(name = "next") val cursor: String? = null,
    @field:Json(name = "items") val items: List<ApiFilmSummary>
)
