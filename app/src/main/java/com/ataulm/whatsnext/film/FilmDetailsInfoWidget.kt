package com.ataulm.whatsnext.film

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import coil.api.load
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.merge_film_details_info.view.*
import java.util.concurrent.TimeUnit

class FilmDetailsInfoWidget constructor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_film_details_info, this)
    }

    fun bind(film: Film) {
        val director = film.crew.find { contributor -> contributor.type == "Director" }?.person?.name
        if (director != null) {
            film_details_info_text_director.text = director
            film_details_info_text_director.visibility = VISIBLE
        } else {
            film_details_info_text_director.visibility = GONE
        }

        film.tagline?.let {
            film_details_info_text_tagline.text = it
            film_details_info_text_tagline.visibility = VISIBLE
        } ?: run {
            film_details_info_text_tagline.visibility = GONE
        }

        film.description?.let {
            film_details_info_text_description.text = it
            film_details_info_text_description.visibility = VISIBLE
        } ?: run {
            film_details_info_text_description.visibility = GONE
        }

        durationText(film)?.let {
            film_details_info_text_duration.text = it
            film_details_info_text_duration.visibility = VISIBLE
            film_details_info_text_label_duration.visibility = VISIBLE
        } ?: run {
            film_details_info_text_duration.visibility = GONE
            film_details_info_text_label_duration.visibility = GONE
        }

        genresText(film)?.let {
            film_details_info_text_genres.text = it
            film_details_info_text_genres.visibility = VISIBLE
            film_details_info_text_label_genres.visibility = VISIBLE
        } ?: run {
            film_details_info_text_genres.visibility = GONE
            film_details_info_text_label_genres.visibility = GONE
        }

        film_details_info_image_poster
                .load(film.poster.bestFor(film_details_info_image_poster.width)?.url)
    }

    fun bind(filmSummary: FilmSummary) {
        val director = filmSummary.directors.firstOrNull()?.name
        if (director != null) {
            film_details_info_text_director.text = director
            film_details_info_text_director.visibility = VISIBLE
        } else {
            film_details_info_text_director.visibility = GONE
        }

        film_details_info_text_tagline.visibility = GONE
        film_details_info_text_description.visibility = GONE
        film_details_info_text_duration.visibility = GONE
        film_details_info_text_label_duration.visibility = GONE
        film_details_info_text_genres.visibility = GONE
        film_details_info_text_label_genres.visibility = GONE

        film_details_info_image_poster
                .load(filmSummary.poster.bestFor(film_details_info_image_poster.width)?.url)
    }

    private fun durationText(film: Film): String? {
        film.runtimeMinutes?.let {
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

    private fun genresText(film: Film): String? {
        return if (film.genres.isNotEmpty()) {
            film.genres.joinToString(", ")
        } else {
            null
        }
    }
}
