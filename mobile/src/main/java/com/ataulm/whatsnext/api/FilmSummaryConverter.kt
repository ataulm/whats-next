package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.FilmSummary

internal class FilmSummaryConverter() {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(apiFilmSummary.letterboxId, apiFilmSummary.name, apiFilmSummary.releaseYear.toString());
    }
}
