package com.ataulm.whatsnext

data class FilmSummary(
        val ids: Ids,
        val name: String,
        val year: String,
        val poster: Images,
        val backdrop: Images,
        val cast: List<Actor>,
        val crew: List<Contributor>
)
