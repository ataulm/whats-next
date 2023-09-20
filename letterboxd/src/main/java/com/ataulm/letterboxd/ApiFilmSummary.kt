package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilmSummary(
    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "releaseYear")
    val releaseYear: Int?,

    @field:Json(name = "directors")
    val directors: List<ApiContributor>?,

    @field:Json(name = "poster")
    val poster: ApiImage?,

    @field:Json(name = "links")
    val links: List<ApiLink>
)
