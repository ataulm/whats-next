package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiMeResponse(
    @field:Json(name = "member") val member: ApiMember
)
