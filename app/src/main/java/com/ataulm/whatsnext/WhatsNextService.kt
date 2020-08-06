package com.ataulm.whatsnext

import com.ataulm.whatsnext.api.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

internal class WhatsNextService(
        private val letterboxdApi: LetterboxdApi,
        private val filmSummaryConverter: FilmSummaryConverter,
        private val filmRelationshipConverter: FilmRelationshipConverter
) {

    suspend fun login(username: String, password: String): Token {
        return withContext(Dispatchers.IO) {
            val authTokenApiResponse = letterboxdApi.fetchAuthToken(username, password)
            Token(authTokenApiResponse.accessToken, authTokenApiResponse.refreshToken)
        }
    }

    suspend fun search(searchTerm: String): List<FilmSummary> {
        return withContext(Dispatchers.IO) {
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
    }

    fun film(letterboxdId: String): Observable<Film> {
        return Single.zip(
                letterboxdApi.film(letterboxdId),
                letterboxdApi.filmRelationship(letterboxdId),
                BiFunction<ApiFilm, ApiFilmRelationship, Film> { apiFilm, apiFilmRelationship ->
                    Film(
                            filmSummaryConverter.convert(apiFilm),
                            filmRelationshipConverter.convert(apiFilmRelationship)
                    )
                }
        ).toObservable()
    }

}
