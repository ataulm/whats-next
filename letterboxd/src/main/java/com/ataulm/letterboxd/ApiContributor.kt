package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiContributor(
    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "name")
    val name: String,

    /**
     * Only available for actors
     */
    @field:Json(name = "characterName")
    val characterName: String?
)
