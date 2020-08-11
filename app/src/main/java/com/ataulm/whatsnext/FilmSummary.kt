package com.ataulm.whatsnext

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilmSummary(
        val ids: Ids,
        val name: String,
        val year: String?,
        val runtimeMinutes: Int?,
        val tagline: String?,
        val description: String?,
        val poster: Images,
        val genres: List<String>,
        val cast: List<Actor>,
        val crew: List<Contributor>
) : Parcelable {

    fun director(): Person? {
        return crew.find { contributor -> contributor.type == "Director" }?.person
    }
}


