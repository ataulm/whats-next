package com.ataulm.whatsnext

data class Poster(private val images: List<Image>) {

    fun forWidth(width: Int): Image? {
        // TODO: this should calculate the best image that fits the prescribed width
        return if (images.isNotEmpty()) images[0] else null
    }
}
