package com.ataulm.whatsnext.film

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.Person
import com.ataulm.whatsnext.R
import com.bumptech.glide.Glide

class FilmDetailsInfoWidget constructor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    @BindView(R.id.film_details_info_text_title) lateinit var titleTextView: TextView
    @BindView(R.id.film_details_info_text_release_year_director) lateinit var releaseYearTextView: TextView
    @BindView(R.id.film_details_info_text_runtime) lateinit var runtimeTextView: TextView
    @BindView(R.id.film_details_info_image_poster) lateinit var posterImageView: ImageView
    @BindView(R.id.film_details_info_text_tagline) lateinit var taglineTextView: TextView
    @BindView(R.id.film_details_info_text_description) lateinit var descriptionTextView: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_film_details_info, this)
        ButterKnife.bind(this)
    }

    fun bind(filmSummary: FilmSummary) {
        titleTextView.text = filmSummary.name

        releaseYearText(filmSummary)?.let {
            releaseYearTextView.text = it
            releaseYearTextView.visibility = VISIBLE
        } ?: run {
            releaseYearTextView.visibility = GONE
        }

        filmSummary.tagline?.let {
            taglineTextView.text = it
            taglineTextView.visibility = VISIBLE
        } ?: run {
            taglineTextView.visibility = GONE
        }

        filmSummary.description?.let {
            descriptionTextView.text = it
            descriptionTextView.visibility = VISIBLE
        } ?: run {
            descriptionTextView.visibility = GONE
        }

        Glide.with(posterImageView.context)
                .load(filmSummary.poster.bestFor(posterImageView.width)?.url)
                .into(posterImageView)
    }


    private fun releaseYearText(filmSummary: FilmSummary): String? {
        director(filmSummary)?.name?.let { directorName ->
            filmSummary.year?.let { year ->
                return resources.getString(R.string.film_details_release_year_director_format, year, directorName)
            }
            return directorName
        }
        return filmSummary.year
    }

    private fun director(filmSummary: FilmSummary): Person? {
        return filmSummary.crew.find { contributor -> contributor.type == "Director" }?.person
    }
}
