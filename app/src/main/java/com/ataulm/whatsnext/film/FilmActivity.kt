package com.ataulm.whatsnext.film

import android.os.Bundle
import android.view.View
import com.ataulm.support.DataObserver
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.di.DaggerFilmComponent
import com.ataulm.whatsnext.di.appComponent
import com.ataulm.whatsnext.film.FilmViewModel.FilmDetailsUiModel
import kotlinx.android.synthetic.main.activity_film.*
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
        titleTextView.text = if (film.filmSummary.year != null) {
            "${film.filmSummary.name} (${film.filmSummary.year})"
        } else {
            film.filmSummary.name
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

    companion object {

        const val EXTRA_FILM_ID = BuildConfig.APPLICATION_ID + ".EXTRA_FILM_ID"
    }
}

private fun FilmActivity.injectDependencies() {
    val filmId = checkNotNull(intent.getStringExtra(FilmActivity.EXTRA_FILM_ID)) {
        "how you open FilmActivity without a film id?"
    }
    DaggerFilmComponent.builder()
            .activity(this)
            .with(filmId)
            .appComponent(appComponent())
            .build()
            .inject(this)
}
