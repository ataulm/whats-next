package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ApiFilmsResponse(
    @field:Json(name = "next")
    val cursorToNextPageOfResults: String,

    @field:Json(name = "items")
    val filmSummaries: List<ApiFilmSummary>
)
