package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmSummary(
        val ids: Ids,
        val name: String,
        val year: String?,
        val poster: Images,
        val directors: List<Person>
) : Parcelable
