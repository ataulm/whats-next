package com.ataulm.whatsnext

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmStats(
        val rating: Float,
        val counts: Counts,
        val ratingsHistogram: List<RatingsHistogramBar>
) : Parcelable {

    @Parcelize
    data class Counts(val watches: Int, val likes: Int, val ratings: Int) : Parcelable

    @Parcelize
    data class RatingsHistogramBar(val rating: Float, val weight: Float, val count: Int) : Parcelable
}
