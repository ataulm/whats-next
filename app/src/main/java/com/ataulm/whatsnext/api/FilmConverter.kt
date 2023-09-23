package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.model.Actor
import com.ataulm.letterboxd.ApiContributor
import com.ataulm.letterboxd.ApiFilm
import com.ataulm.letterboxd.ApiImage
import com.ataulm.whatsnext.model.Contributor
import com.ataulm.whatsnext.model.Film
import com.ataulm.whatsnext.model.Ids
import com.ataulm.whatsnext.model.Image
import com.ataulm.whatsnext.model.Images
import com.ataulm.whatsnext.model.Person

class FilmConverter {

    fun convert(apiFilm: ApiFilm): Film {
        return Film(
                ids(apiFilm),
                apiFilm.name,
                apiFilm.releaseYear.toString(),
                apiFilm.runtimeMinutes,
                apiFilm.tagline,
                apiFilm.description,
                images(apiFilm.poster),
                genres(apiFilm),
                cast(apiFilm),
                crew(apiFilm)
        )
    }

    private fun ids(apiFilm: ApiFilm): Ids {
        val letterboxd = apiFilm.links.first { link -> link.type == "letterboxd" }.id
        val imdb = apiFilm.links.find { link -> link.type == "imdb" }?.id
        val tmdb = apiFilm.links.find { link -> link.type == "tmdb" }?.id
        return Ids(letterboxd, imdb, tmdb)
    }

    private fun images(apiImage: ApiImage?): Images {
        if (apiImage == null) {
            return Images(emptyList())
        }
        return Images(apiImage.sizes.map { Image(it.width, it.height, it.url) })
    }

    private fun genres(apiFilm: ApiFilm): List<String> {
        apiFilm.genres?.let {
            return it.map { apiGenre -> apiGenre.name }
        }
        return emptyList()
    }

    private fun cast(apiFilm: ApiFilm): List<Actor> {
        return apiFilm.contributions
                ?.filter { it.type == "Actor" }
                ?.flatMap { it.contributors }
                ?.map { Actor(it.characterName, Person(it.id, it.name)) }
                .orEmpty()
    }

    private fun crew(apiFilm: ApiFilm): List<Contributor> {
        return apiFilm.contributions
                ?.filterNot { it.type == "Actor" }
                ?.flatMap { contributors(it.type, it.contributors) }
                .orEmpty()
    }

    private fun contributors(type: String, apiContributors: List<ApiContributor>): List<Contributor> {
        return apiContributors.map { Contributor(type, Person(it.id, it.name)) }
    }
}
