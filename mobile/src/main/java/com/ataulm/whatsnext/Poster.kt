package com.ataulm.whatsnext

data class Poster(val images: List<Image>) {

    fun forWidth(width: Int): Image? {
        if (images.isNotEmpty()) {
            // TODO: this should calculate the best image that fits the prescribed width
            return images.get(0)
        } else {
            return null
        }
    }
}
