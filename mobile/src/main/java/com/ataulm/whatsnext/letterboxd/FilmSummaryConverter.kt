package com.ataulm.whatsnext.letterboxd

import com.ataulm.whatsnext.FilmSummary

internal class FilmSummaryConverter() {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(apiFilmSummary.letterboxId, apiFilmSummary.name, apiFilmSummary.releaseYear.toString());
    }
}
