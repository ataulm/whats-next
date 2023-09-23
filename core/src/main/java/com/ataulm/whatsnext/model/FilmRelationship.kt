package com.ataulm.whatsnext.model

data class FilmRelationship(
    val watched: Boolean,
    val liked: Boolean,
    val inWatchlist: Boolean,
    val rating: FilmRating
)
