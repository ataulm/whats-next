package com.ataulm.whatsnext

import com.ataulm.whatsnext.api.ApiFilmRelationshipUpdateRequest
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.api.LetterboxdApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

internal class WhatsNextService(
        private val letterboxdApi: LetterboxdApi,
        private val filmSummaryConverter: FilmSummaryConverter,
        private val filmRelationshipConverter: FilmRelationshipConverter
) {

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        val authTokenApiResponse = letterboxdApi.fetchAuthToken(username, password)
        Token(authTokenApiResponse.accessToken, authTokenApiResponse.refreshToken)
    }

    suspend fun search(searchTerm: String) = withContext(Dispatchers.IO) {
        val apiSearchResponse = letterboxdApi.search(searchTerm)
        val filmSummaries: MutableList<FilmSummary> = ArrayList(apiSearchResponse.searchItems.size)
        for (searchItem in apiSearchResponse.searchItems) {
            if ("FilmSearchItem" != searchItem.type) {
                continue
            }
            val filmSummary = filmSummaryConverter.convert(searchItem.filmSummary)
            filmSummaries.add(filmSummary)
        }
        filmSummaries
    }

    suspend fun film(letterboxdId: String) = withContext(Dispatchers.IO) {
        val apiFilm = letterboxdApi.film(letterboxdId)
        val apiFilmRelationship = letterboxdApi.filmRelationship(letterboxdId)
        Film(
                filmSummaryConverter.convert(apiFilm),
                filmRelationshipConverter.convert(apiFilmRelationship)
        )
    }

    suspend fun updateFilmRelationship(
            letterboxdId: String,
            watched: Boolean,
            liked: Boolean,
            inWatchlist: Boolean,
            rating: FilmRating
    ) = withContext(Dispatchers.IO) {
        letterboxdApi.updateFilmRelationship(
                letterboxdId,
                request = ApiFilmRelationshipUpdateRequest(
                        watched = watched,
                        liked = liked ,
                        inWatchlist = inWatchlist,
                        rating = rating.toApiValue()
                )
        )
        val apiFilm = letterboxdApi.film(letterboxdId)
        val apiFilmRelationship = letterboxdApi.filmRelationship(letterboxdId)
        Film(
                filmSummaryConverter.convert(apiFilm),
                filmRelationshipConverter.convert(apiFilmRelationship)
        )
    }
}

/**
 * Using String so that we can send `"null"` as a value (valid for the API). `null` as in
 * "not present" will be stripped out by Retrofit.
 */
private fun FilmRating.toApiValue(): String {
    return when (this) {
        FilmRating.UNRATED -> "null"
        FilmRating.HALF -> "0.5"
        FilmRating.ONE -> "1"
        FilmRating.ONE_HALF -> "1.5"
        FilmRating.TWO -> "2"
        FilmRating.TWO_HALF -> "2.5"
        FilmRating.THREE -> "3"
        FilmRating.THREE_HALF -> "3.5"
        FilmRating.FOUR -> "4"
        FilmRating.FOUR_HALF -> "4.5"
        FilmRating.FIVE -> "5"
    }
}
