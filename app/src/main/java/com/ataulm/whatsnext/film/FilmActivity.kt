package com.ataulm.whatsnext.film

import android.os.Bundle
import android.view.View
import coil.api.load
import com.ataulm.support.DataObserver
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.DaggerFilmComponent
import com.ataulm.whatsnext.di.appComponent
import com.ataulm.whatsnext.film.FilmViewModel.FilmDetailsUiModel
import kotlinx.android.synthetic.main.activity_film.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FilmActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModel: FilmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_film)

        viewModel.filmDetails.observe(this, DataObserver<FilmDetailsUiModel> {
            display(it)
        })
    }

    private fun display(film: FilmDetailsUiModel) {
        titleTextView.text = if (film.releaseYear != null) {
            getString(R.string.title_and_year, film.title, film.releaseYear)
        } else {
            film.title
        }

        val directors = film.directors()
        if (directors != null) {
            directedByTextView.text = getString(R.string.directed_by, directors)
            directedByTextView.visibility = View.VISIBLE
        } else {
            directedByTextView.visibility = View.GONE
        }

        if (film.filmStats != null) {
            ratingTextView.text = film.filmStats.rating.stars()
            // same as text but it chooses between star/stars correctly!
            ratingTextView.contentDescription = film.filmStats.rating.starsContentDesc()

            val formattedRatings = DecimalFormat("#,###").format(film.filmStats.counts.ratings)
            ratingsCountTextView.text = getString(R.string.ratings, formattedRatings)
            ratingsCountTextView.contentDescription = getString(R.string.ratings_content_description, formattedRatings)

            ratingsDistributionWidget.show(film.filmStats.ratingsHistogram)

            // TODO: can we do this in a custom layout inflater such that it works in XML?
            filmStatsGroup.clipToOutline = true
            ratingsTextGroup.visibility = View.VISIBLE
            ratingsDistributionWidget.visibility = View.VISIBLE
        } else {
            ratingsTextGroup.visibility = View.INVISIBLE
            ratingsDistributionWidget.visibility = View.INVISIBLE
        }

        posterImageView.clipToOutline = true
        posterImageView
                .load(film.poster.bestFor(posterImageView.width)?.url)

        durationText(film.film)?.let {
            durationTextView.text = it
            durationTextView.visibility = View.VISIBLE
            durationLabelTextView.visibility = View.VISIBLE
        } ?: run {
            durationTextView.visibility = View.GONE
            durationLabelTextView.visibility = View.GONE
        }

        genresText(film.film)?.let {
            genresTextView.text = it
            genresTextView.visibility = View.VISIBLE
            genresLabelTextView.visibility = View.VISIBLE
        } ?: run {
            genresTextView.visibility = View.GONE
            genresLabelTextView.visibility = View.GONE
        }

        if (film.film != null) {
            filmDetailsInfoWidget.bind(film.film)
        }

        if (film.filmRelationship != null) {
            likeCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.setOnCheckedChangeListener(null)
            ratingBar.onRatingBarChangeListener = null

            likeCheckBox.isChecked = film.filmRelationship.liked
            watchedCheckBox.isChecked = film.filmRelationship.watched
            ratingBar.rating = film.filmRelationship.rating.toFloat()

            likeCheckBox.setOnCheckedChangeListener { _, liked -> viewModel.onClickLiked(liked) }
            watchedCheckBox.setOnCheckedChangeListener { _, watched -> viewModel.onClickWatched(watched) }
            ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
                if (fromUser) {
                    viewModel.onClickRating(rating)
                }
            }

            likeCheckBox.visibility = View.VISIBLE
            watchedCheckBox.visibility = View.VISIBLE
            ratingBar.visibility = View.VISIBLE
        } else {
            likeCheckBox.visibility = View.GONE
            watchedCheckBox.visibility = View.GONE
            ratingBar.visibility = View.GONE
        }
    }

    private fun FilmDetailsUiModel.directors(): String? {
        if (directors.isEmpty()) return null
        if (directors.size == 1) return directors.first()
        val builder = StringBuilder()
        for (i in directors.indices) {
            val nextPart = when (i) {
                0 -> directors[0]
                directors.size - 1 -> " & ${directors[i]}"
                else -> ", ${directors[i]}"
            }
            builder.append(nextPart)
        }
        return builder.toString()
    }

    private fun Float.stars() = getString(R.string.stars, formattedNumberOfStars())

    private fun Float.starsContentDesc() = resources.getQuantityString(
            R.plurals.stars_content_description,
            toInt(),
            formattedNumberOfStars()
    )

    /**
     * Returns rating as string, formatted X or X.X (e.g. 4 or 4.5)
     */
    private fun Float.formattedNumberOfStars() = if (this == toInt().toFloat()) {
        toInt().toString()
    } else {
        "%.1f".format(this)
    }

    private fun durationText(film: Film?): String? {
        return film?.runtimeMinutes?.let {
            if (TimeUnit.MINUTES.toHours(it.toLong()) > 0) {
                val hours = TimeUnit.MINUTES.toHours(it.toLong())
                val minutes = it.toLong() - TimeUnit.HOURS.toMinutes(hours)
                resources.getString(R.string.film_details_duration_hours_and_minutes_format, hours.toString(), minutes.toString())
            } else {
                resources.getString(R.string.film_details_duration_minutes_format, it.toString())
            }
        }
    }

    private fun genresText(film: Film?): String? {
        if (film == null) return null
        return if (film.genres.isNotEmpty()) {
            film.genres.joinToString(", ")
        } else {
            null
        }
    }

    companion object {

        const val EXTRA_FILM_SUMMARY = BuildConfig.APPLICATION_ID + ".EXTRA_FILM_SUMMARY"
    }
}

private fun FilmActivity.injectDependencies() {
    val filmSummary = checkNotNull(intent.getParcelableExtra(FilmActivity.EXTRA_FILM_SUMMARY) as? FilmSummary) {
        "how you open FilmActivity without a film id?"
    }
    DaggerFilmComponent.builder()
            .activity(this)
            .with(filmSummary)
            .appComponent(appComponent())
            .build()
            .inject(this)
}
