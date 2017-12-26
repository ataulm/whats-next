package com.ataulm.whatsnext

data class FilmSummary(
        val ids: Ids,
        val name: String,
        val year: String?,
        val runtimeMinutes: Int?,
        val tagline: String?,
        val description: String?,
        val poster: Images,
        val backdrop: Images,
        val cast: List<Actor>,
        val crew: List<Contributor>
) {

    fun director(): Person? {
        return crew.find { contributor -> contributor.type == "Director" }?.person
    }
}


