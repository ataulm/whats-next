package com.ataulm.whatsnext.model

import java.io.Serializable

data class FilmStats(
    val rating: Float,
    val counts: Counts,
    val ratingsHistogram: List<RatingsHistogramBar>
) : Serializable {

    data class Counts(val watches: Int, val likes: Int, val ratings: Int) : Serializable

    data class RatingsHistogramBar(val rating: Float, val weight: Float, val count: Int) :
        Serializable
}
