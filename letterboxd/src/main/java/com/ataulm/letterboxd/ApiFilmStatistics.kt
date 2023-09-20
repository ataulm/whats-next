package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilmStatistics(
    @field:Json(name = "rating") val rating: Float? = null,
    @field:Json(name = "counts") val counts: ApiFilmStatisticsCounts,
    @field:Json(name = "ratingsHistogram") val ratingsHistogram: List<ApiRatingsHistogramBar>
)
