package com.ataulm.whatsnext.model

import java.io.Serializable

data class FilmSummary(
        val ids: Ids,
        val name: String,
        val year: String?,
        val poster: Images,
        val directors: List<Person>
) : Serializable
