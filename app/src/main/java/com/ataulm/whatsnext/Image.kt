package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(val width: Int, val height: Int, val url: String): Parcelable
