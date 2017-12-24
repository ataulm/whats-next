package com.ataulm.whatsnext

data class Images(private val images: List<Image>) {

    fun bestFor(width: Int, height: Int): Image? {
        // TODO: this should calculate the best image that fits the prescribed width
        return if (images.isNotEmpty()) images[0] else null
    }
}
