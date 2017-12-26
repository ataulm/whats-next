package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.*

internal class FilmSummaryConverter {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(
                ids(apiFilmSummary),
                apiFilmSummary.name,
                apiFilmSummary.releaseYear.toString(),
                apiFilmSummary.runtimeMinutes,
                apiFilmSummary.tagline,
                apiFilmSummary.description,
                images(apiFilmSummary.poster),
                images(apiFilmSummary.backdrop),
                cast(apiFilmSummary),
                crew(apiFilmSummary)
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

    private fun cast(apiFilmSummary: ApiFilmSummary): List<Actor> {
        // TODO: parse the actors
        return emptyList()
    }

    private fun crew(apiFilmSummary: ApiFilmSummary): List<Contributor> {
        // TODO: parse the crew
        return emptyList()
    }
}
