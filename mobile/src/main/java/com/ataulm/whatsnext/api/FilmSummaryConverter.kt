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
                genres(apiFilmSummary),
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

    private fun genres(apiFilmSummary: ApiFilmSummary): List<String> {
        apiFilmSummary.genres?.let {
            return it.map { apiGenre -> apiGenre.name }
        }
        return emptyList()
    }

    private fun cast(apiFilmSummary: ApiFilmSummary): List<Actor> {
        return apiFilmSummary.contributions
                ?.filter { it.type == "Actor" }
                ?.flatMap { it.contributors }
                ?.map { Actor(it.characterName, Person(it.id, it.name)) }
                .orEmpty()
    }

    private fun crew(apiFilmSummary: ApiFilmSummary): List<Contributor> {
        return apiFilmSummary.contributions
                ?.filterNot { it.type == "Actor" }
                ?.flatMap { contributors(it.type, it.contributors) }
                .orEmpty()
    }

    private fun contributors(type: String, apiContributors: List<ApiContributor>): List<Contributor> {
        return apiContributors.map { Contributor(type, Person(it.id, it.name)) }
    }
}
