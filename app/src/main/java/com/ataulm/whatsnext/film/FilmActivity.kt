package com.ataulm.whatsnext.film

import android.os.Bundle
import com.ataulm.support.DataObserver
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.di.DaggerFilmComponent
import com.ataulm.whatsnext.di.appComponent
import kotlinx.android.synthetic.main.activity_film.*
import javax.inject.Inject

class FilmActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModel: FilmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_film)

        viewModel.film.observe(this, DataObserver<Film> {
            display(it)
        })
    }

    // TODO: checkboxes/ratings don't do anything yet
    private fun display(film: Film) {
        titleTextView.text = if (film.summary.year != null) {
            "${film.summary.name} (${film.summary.year})"
        } else {
            film.summary.name
        }
        likeCheckBox.isChecked = film.relationship.liked
        watchedCheckBox.isChecked = film.relationship.watched
        ratingBar.rating = film.relationship.rating.toFloat()
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
