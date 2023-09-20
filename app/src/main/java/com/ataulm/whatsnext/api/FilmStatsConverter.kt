package com.ataulm.whatsnext.api

import com.ataulm.letterboxd.ApiFilmStatistics
import com.ataulm.letterboxd.ApiFilmStatisticsCounts
import com.ataulm.letterboxd.ApiRatingsHistogramBar
import com.ataulm.whatsnext.FilmStats

class FilmStatsConverter {

    fun convert(apiFilmStatistics: ApiFilmStatistics): FilmStats? {
        val rating = apiFilmStatistics.rating ?: return null
        return FilmStats(
            rating = rating,
            counts = counts(apiFilmStatistics.counts),
            ratingsHistogram = ratingsHistogram(apiFilmStatistics.ratingsHistogram)
        )
    }

    private fun counts(apiFilmStatisticsCounts: ApiFilmStatisticsCounts) =
        apiFilmStatisticsCounts.let {
            FilmStats.Counts(
                watches = it.watches,
                likes = it.likes,
                ratings = it.ratings
            )
        }

    private fun ratingsHistogram(apiHistogram: List<ApiRatingsHistogramBar>) = apiHistogram.map {
        FilmStats.RatingsHistogramBar(
            rating = it.rating,
            weight = it.weight,
            count = it.count
        )
    }
}
