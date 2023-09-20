package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiContribution(
    @field:Json(name = "type")
    val type: String,

    @field:Json(name = "contributors")
    val contributors: List<ApiContributor>
)
