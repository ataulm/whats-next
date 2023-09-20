package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchResponse(
    @field:Json(name = "next")
    val cursorToNextPageOfResults: String,

    @field:Json(name = "items")
    val searchItems: List<Result>
) {

    @JsonClass(generateAdapter = true)
    class Result(
        @field:Json(name = "type")
        val type: String,

        @field:Json(name = "film")
        val filmSummary: ApiFilmSummary
    )
}
