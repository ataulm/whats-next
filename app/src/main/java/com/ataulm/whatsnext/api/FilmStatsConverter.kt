package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.FilmStats

internal class FilmStatsConverter {

    fun convert(apiFilmStatistics: ApiFilmStatistics): FilmStats? {
        if (apiFilmStatistics.rating == null) {
            return null
        }
        return FilmStats(
                rating = apiFilmStatistics.rating,
                counts = counts(apiFilmStatistics.counts),
                ratingsHistogram = ratingsHistogram(apiFilmStatistics.ratingsHistogram)
        )
    }

    private fun counts(apiFilmStatisticsCounts: ApiFilmStatisticsCounts) = apiFilmStatisticsCounts.let {
        FilmStats.Counts(
                watches = it.watches,
                likes = it.likes
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
