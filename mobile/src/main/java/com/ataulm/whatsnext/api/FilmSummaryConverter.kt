package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.Ids
import com.ataulm.whatsnext.Image
import com.ataulm.whatsnext.Images

internal class FilmSummaryConverter {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(
                ids(apiFilmSummary),
                apiFilmSummary.name,
                apiFilmSummary.releaseYear.toString(),
                images(apiFilmSummary.poster),
                images(apiFilmSummary.backdrop),
                emptyList(),
                emptyList()
        )
    }

    private fun ids(apiFilmSummary: ApiFilmSummary): Ids {
        val letterboxd = apiFilmSummary.links.first { link -> link.type == "letterboxd" }.id
        val imdb = apiFilmSummary.links.firstOrNull { link -> link.type == "imdb" }?.id
        val tmdb = apiFilmSummary.links.firstOrNull { link -> link.type == "tmdb" }?.id
        return Ids(letterboxd, imdb, tmdb)
    }

    private fun images(apiImage: ApiImage?): Images {
        if (apiImage == null) {
            return Images(emptyList())
        }
        return Images(apiImage.sizes.map { Image(it.width, it.height, it.url) })
    }
}
