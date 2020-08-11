package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(val id: String, val name: String): Parcelable
