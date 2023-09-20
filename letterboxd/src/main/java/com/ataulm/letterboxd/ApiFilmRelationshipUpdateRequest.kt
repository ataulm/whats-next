package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiFilmRelationshipUpdateRequest(
    @field:Json(name = "watched") val watched: Boolean,
    @field:Json(name = "liked") val liked: Boolean,
    @field:Json(name = "inWatchlist") val inWatchlist: Boolean,
    /**
     * Accepts values between 0.5 and 5.0, with increments of 0.5, or null (to remove the
     * rating). If set, [watched] is assumed to be true.
     */
    @field:Json(name = "rating") val rating: String
)
