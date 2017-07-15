package com.ataulm.whatsnext.letterboxd

import com.ataulm.whatsnext.FilmSummary

internal class FilmConverter() {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(apiFilmSummary.letterboxId, apiFilmSummary.name, apiFilmSummary.releaseYear.toString());
    }
}
