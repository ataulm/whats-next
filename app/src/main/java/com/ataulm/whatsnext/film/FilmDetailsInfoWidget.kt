package com.ataulm.whatsnext.film

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import coil.api.load
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.merge_film_details_info.view.*
import java.util.concurrent.TimeUnit

class FilmDetailsInfoWidget constructor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_film_details_info, this)
    }

    fun bind(filmSummary: FilmSummary) {
        film_details_info_text_title.text = filmSummary.name

        releaseYearText(filmSummary)?.let {
            film_details_info_text_release_year_director.text = it
            film_details_info_text_release_year_director.visibility = VISIBLE
        } ?: run {
            film_details_info_text_release_year_director.visibility = GONE
        }

        filmSummary.tagline?.let {
            film_details_info_text_tagline.text = it
            film_details_info_text_tagline.visibility = VISIBLE
        } ?: run {
            film_details_info_text_tagline.visibility = GONE
        }

        filmSummary.description?.let {
            film_details_info_text_description.text = it
            film_details_info_text_description.visibility = VISIBLE
        } ?: run {
            film_details_info_text_description.visibility = GONE
        }

        durationText(filmSummary)?.let {
            film_details_info_text_duration.text = it
            film_details_info_text_duration.visibility = VISIBLE
            film_details_info_text_label_duration.visibility = VISIBLE
        } ?: run {
            film_details_info_text_duration.visibility = GONE
            film_details_info_text_label_duration.visibility = GONE
        }

        genresText(filmSummary)?.let {
            film_details_info_text_genres.text = it
            film_details_info_text_genres.visibility = VISIBLE
            film_details_info_text_label_genres.visibility = VISIBLE
        } ?: run {
            film_details_info_text_genres.visibility = GONE
            film_details_info_text_label_genres.visibility = GONE
        }

        film_details_info_image_poster
                .load(filmSummary.poster.bestFor(film_details_info_image_poster.width)?.url)
    }

    // TODO: this should be ready as `String?` in a viewmodel
    private fun releaseYearText(filmSummary: FilmSummary): String? {
        filmSummary.director()?.name?.let { directorName ->
            filmSummary.year?.let { year ->
                return resources.getString(R.string.film_details_release_year_director_format, year, directorName)
            }
            return directorName
        }
        return filmSummary.year
    }

    // TODO: this should be ready as `String?` in a viewmodel
    private fun durationText(filmSummary: FilmSummary): String? {
        filmSummary.runtimeMinutes?.let {
            return if (TimeUnit.MINUTES.toHours(it.toLong()) > 0) {
                val hours = TimeUnit.MINUTES.toHours(it.toLong())
                val minutes = it.toLong() - TimeUnit.HOURS.toMinutes(hours)
                resources.getString(R.string.film_details_duration_hours_and_minutes_format, hours.toString(), minutes.toString())
            } else {
                resources.getString(R.string.film_details_duration_minutes_format, it.toString())
            }
        }
        return null
    }

    // TODO: this should be ready as `String?` in a viewmodel
    private fun genresText(filmSummary: FilmSummary): String? {
        return if (filmSummary.genres.isNotEmpty()) {
            filmSummary.genres.joinToString(", ")
        } else {
            null
        }
    }
}
