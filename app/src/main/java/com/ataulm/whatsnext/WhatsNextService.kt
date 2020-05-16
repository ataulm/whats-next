package com.ataulm.whatsnext

import com.ataulm.whatsnext.api.ApiFilm
import com.ataulm.whatsnext.api.ApiFilmRelationship
import com.ataulm.whatsnext.api.AuthTokenApiResponse
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.api.LetterboxdApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.util.*

internal class WhatsNextService(
        private val letterboxdApi: LetterboxdApi,
        private val filmSummaryConverter: FilmSummaryConverter,
        private val filmRelationshipConverter: FilmRelationshipConverter
) {

    fun login(username: String, password: String): Observable<Token> {
        return letterboxdApi.fetchAuthToken(username, password)
                .toObservable()
                .map(toToken())
    }

    private fun toToken(): Function<AuthTokenApiResponse, Token> = Function { (accessToken, refreshToken) ->
        Token(accessToken, refreshToken)
    }

    fun search(searchTerm: String): Observable<List<FilmSummary>> {
        return letterboxdApi.search(searchTerm)
                .toObservable()
                .map { apiSearchResponse ->
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

internal class UserRequiresSignInException : RuntimeException()