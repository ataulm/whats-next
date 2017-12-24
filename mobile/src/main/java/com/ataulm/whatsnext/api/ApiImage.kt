package com.ataulm.whatsnext.api

import com.google.gson.annotations.SerializedName

internal class ApiImage {

    @SerializedName("sizes")
    lateinit var sizes: List<Size>

    internal class Size {

        @SerializedName("width")
        var width: Int = 0

        @SerializedName("height")
        var height: Int = 0

        @SerializedName("url")
        lateinit var url: String
    }
}
