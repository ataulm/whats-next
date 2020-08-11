package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ids(val letterboxd: String, val imdb: String?, val tmdb: String?) : Parcelable
