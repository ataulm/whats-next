package com.ataulm.whatsnext.letterboxd

import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.Token
import java.util.concurrent.TimeUnit

internal class FilmConverter() {

    fun convert(apiFilmSummary: ApiFilmSummary): Film {
        return Film(apiFilmSummary.letterboxId, apiFilmSummary.name, apiFilmSummary.releaseYear.toString());
    }
}
