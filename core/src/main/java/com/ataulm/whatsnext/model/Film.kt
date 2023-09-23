package com.ataulm.whatsnext.model

import java.io.Serializable

data class Film(
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
) : Serializable
