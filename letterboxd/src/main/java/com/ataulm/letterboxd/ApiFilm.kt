package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilm(
    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "tagline")
    val tagline: String?,

    @field:Json(name = "description")
    val description: String?,

    @field:Json(name = "releaseYear")
    val releaseYear: Int?,

    @field:Json(name = "runTime")
    val runtimeMinutes: Int?,

    @field:Json(name = "poster")
    val poster: ApiImage?,

    @field:Json(name = "backdrop")
    val backdrop: ApiImage?,

    @field:Json(name = "links")
    val links: List<ApiLink>,

    @field:Json(name = "genres")
    val genres: List<ApiGenre>?,

    @field:Json(name = "contributions")
    val contributions: List<ApiContribution>?
)
