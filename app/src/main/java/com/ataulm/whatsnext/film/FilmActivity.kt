package com.ataulm.whatsnext.film

import android.os.Bundle
import android.view.View
import com.ataulm.support.DataObserver
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.di.DaggerFilmComponent
import com.ataulm.whatsnext.di.appComponent
import com.ataulm.whatsnext.film.FilmViewModel.FilmDetailsUiModel
import kotlinx.android.synthetic.main.activity_film.*
import java.text.DecimalFormat
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

        if (film.filmStats != null) {
            ratingTextView.text = film.filmStats.rating.stars()
            // same as text but it chooses between star/stars correctly!
            ratingTextView.contentDescription = film.filmStats.rating.starsContentDesc()

            val formattedRatings = DecimalFormat("#,###").format(film.filmStats.counts.ratings)
            ratingsCountTextView.text = getString(R.string.ratings, formattedRatings)

            ratingsDistributionWidget.show(film.filmStats.ratingsHistogram)

            filmStatsGroup.clipToOutline = true
            filmStatsGroup.visibility = View.VISIBLE
        } else {
            filmStatsGroup.visibility = View.GONE
        }

        if (film.film == null) {
            filmDetailsInfoWidget.bind(film.filmSummary)
        } else {
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
