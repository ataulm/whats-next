package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contributor(val type: String, val person: Person): Parcelable
