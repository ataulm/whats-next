package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilmRelationshipUpdateResponse(
    @field:Json(name = "data") val data: ApiFilmRelationship
)