package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.FilmRating
import com.ataulm.whatsnext.FilmRelationship

class FilmRelationshipConverter {

    fun convert(apiFilmRelationship: ApiFilmRelationship) = FilmRelationship(
            apiFilmRelationship.watched,
            apiFilmRelationship.liked,
            apiFilmRelationship.inWatchlist,
            FilmRating.fromFloat(apiFilmRelationship.rating)
    )
}
