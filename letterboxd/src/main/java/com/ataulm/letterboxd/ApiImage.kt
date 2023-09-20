package com.ataulm.letterboxd

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiImage(

    @field:Json(name = "sizes")
    val sizes: List<Size>
) {

    @JsonClass(generateAdapter = true)
    class Size(

        @field:Json(name = "width")
        val width: Int,

        @field:Json(name = "height")
        val height: Int,

        @field:Json(name = "url")
        val url: String
    )
}
