package com.ataulm.whatsnext.api

import com.ataulm.letterboxd.ApiFilmRelationship
import com.ataulm.whatsnext.model.FilmRating
import com.ataulm.whatsnext.model.FilmRelationship

class FilmRelationshipConverter {

    fun convert(apiFilmRelationship: ApiFilmRelationship) = FilmRelationship(
            apiFilmRelationship.watched,
            apiFilmRelationship.liked,
            apiFilmRelationship.inWatchlist,
            FilmRating.fromFloat(apiFilmRelationship.rating)
    )
}
