package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.Image
import com.ataulm.whatsnext.Poster

internal class FilmSummaryConverter {

    fun convert(apiFilmSummary: ApiFilmSummary): FilmSummary {
        return FilmSummary(apiFilmSummary.letterboxId, apiFilmSummary.name, apiFilmSummary.releaseYear.toString(), poster(apiFilmSummary.poster));
    }

    fun poster(apiImage: ApiImage?): Poster {
        if (apiImage == null) {
            return Poster(emptyList<Image>())
        }
        val images = mutableListOf<Image>()
        for (size in apiImage.sizes) {
            val image = Image(size.width, size.height, size.url)
            images.add(image)
        }
        return Poster(images)
    }
}
