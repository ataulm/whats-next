package com.ataulm.whatsnext

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ataulm.letterboxd.ApiFilmRelationshipUpdateRequest
import com.ataulm.letterboxd.LetterboxdApi
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.api.FilmConverter
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmStatsConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.model.FilmRating
import com.ataulm.whatsnext.model.FilmSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WhatsNextRepository(
    private val letterboxdRepository: LetterboxdRepository,
    private val filmSummaryConverter: FilmSummaryConverter,
    private val filmConverter: FilmConverter,
    private val filmRelationshipConverter: FilmRelationshipConverter,
    private val filmStatsConverter: FilmStatsConverter
) {

    fun watchList(memberId: String): PagingSource<String, FilmSummary> = WatchListPagingSource(
        memberId = memberId,
        letterboxdRepository = letterboxdRepository,
        filmSummaryConverter = filmSummaryConverter
    )

    fun popularFilmsThisWeek(): PagingSource<String, FilmSummary> {
        return PopularFilmsThisWeekPagingSource(letterboxdRepository, filmSummaryConverter)
    }

    suspend fun search(searchTerm: String) = withContext(Dispatchers.IO) {
        val apiSearchResponse = letterboxdRepository.search(searchTerm)
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
        val apiFilm = letterboxdRepository.film(letterboxdId)
        filmConverter.convert(apiFilm)
    }

    suspend fun filmRelationship(letterboxdId: String) = withContext(Dispatchers.IO) {
        val apiFilmRelationship = letterboxdRepository.filmRelationship(letterboxdId)
        filmRelationshipConverter.convert(apiFilmRelationship)
    }

    suspend fun filmStats(letterboxdId: String) = withContext(Dispatchers.IO) {
        val apiFilmStatistics = letterboxdRepository.filmStats(letterboxdId)
        filmStatsConverter.convert(apiFilmStatistics)
    }

    suspend fun updateFilmRelationship(
        letterboxdId: String,
        watched: Boolean,
        liked: Boolean,
        inWatchlist: Boolean,
        rating: FilmRating
    ) = withContext(Dispatchers.IO) {
        val apiFilmRelationshipUpdateResponse = letterboxdRepository.updateFilmRelationship(
            letterboxdId,
            request = ApiFilmRelationshipUpdateRequest(
                watched = watched,
                liked = liked,
                inWatchlist = inWatchlist,
                rating = rating.toApiValue()
            )
        )
        val apiFilmRelationship = apiFilmRelationshipUpdateResponse.data
        filmRelationshipConverter.convert(apiFilmRelationship)
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

private class PopularFilmsThisWeekPagingSource(
    private val letterboxdApi: LetterboxdApi,
    private val filmSummaryConverter: FilmSummaryConverter
) : PagingSource<String, FilmSummary>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, FilmSummary> {
        val response = letterboxdApi.popularFilmsThisWeek(
            cursor = params.key,
            perPage = params.loadSize
        )
        return LoadResult.Page(
            data = response.items.map { filmSummaryConverter.convert(it) },
            prevKey = null, // API only allows paging forwards
            nextKey = response.cursor
        )
    }

    override fun getRefreshKey(state: PagingState<String, FilmSummary>): String? {
        // TODO: this method was from an update, but havent looked into it yet
        return null
    }
}

private class WatchListPagingSource(
    private val memberId: String,
    private val letterboxdRepository: LetterboxdRepository,
    private val filmSummaryConverter: FilmSummaryConverter
) : PagingSource<String, FilmSummary>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, FilmSummary> {
        val memberId = letterboxdRepository.me().member.letterboxdId // TODO: don't leave this here
        val response = letterboxdRepository.watchList(
            memberId = memberId,
            cursor = params.key,
            perPage = params.loadSize
        )
        return LoadResult.Page(
            data = response.items.map { filmSummaryConverter.convert(it) },
            prevKey = null, // API only allows paging forwards
            nextKey = response.cursor
        )
    }

    override fun getRefreshKey(state: PagingState<String, FilmSummary>): String? {
        // TODO: this method was from an update, but havent looked into it yet
        return null
    }
}
