package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(val character: String?, val person: Person): Parcelable
