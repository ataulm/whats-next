package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.FilmRelationship

internal class FilmRelationshipConverter {

    fun convert(apiFilmRelationship: ApiFilmRelationship): FilmRelationship {
        return FilmRelationship(apiFilmRelationship.watched, apiFilmRelationship.liked, apiFilmRelationship.inWatchlist, apiFilmRelationship.rating);
    }
}
