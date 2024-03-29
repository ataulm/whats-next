package com.ataulm.whatsnext.api

import com.ataulm.letterboxd.ApiFilmSummary
import com.ataulm.letterboxd.ApiImage
import com.ataulm.whatsnext.model.FilmSummary
import com.ataulm.whatsnext.model.Ids
import com.ataulm.whatsnext.model.Image
import com.ataulm.whatsnext.model.Images
import com.ataulm.whatsnext.model.Person

class FilmSummaryConverter {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(
                ids(apiFilmSummary),
                apiFilmSummary.name,
                apiFilmSummary.releaseYear.toString(),
                images(apiFilmSummary.poster),
                apiFilmSummary.directors?.map { Person(it.id, it.name) } ?: emptyList()
        )
    }

    private fun ids(apiFilmSummary: ApiFilmSummary): Ids {
        val letterboxd = apiFilmSummary.links.first { link -> link.type == "letterboxd" }.id
        val imdb = apiFilmSummary.links.find { link -> link.type == "imdb" }?.id
        val tmdb = apiFilmSummary.links.find { link -> link.type == "tmdb" }?.id
        return Ids(letterboxd, imdb, tmdb)
    }

    private fun images(apiImage: ApiImage?): Images {
        if (apiImage == null) {
            return Images(emptyList())
        }
        return Images(apiImage.sizes.map { Image(it.width, it.height, it.url) })
    }
}
