package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRatingsHistogramBar(
    @field:Json(name = "rating") val rating: Float,
    @field:Json(name = "normalizedWeight") val weight: Float,
    @field:Json(name = "count") val count: Int
)
