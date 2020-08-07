package com.ataulm.whatsnext.film

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ataulm.support.Toaster
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

        val displayer = FilmDisplayer(
                titleTextView,
                watchedCheckBox,
                likeCheckBox,
                ratingBar
        )

        displayer.attach(object : FilmDisplayer.Callback {
            override fun onClickMarkAsWatched() {
                Toaster.display("on click mark as watched")
                // TODO: call viewModel.onClickMarkAsWatched() here?
            }

            override fun onClickMarkAsNotWatched() {
                Toaster.display("on click mark as not watched")
                // TODO: call viewModel.onClickMarkAsNotWatched() here?
            }
        })

        viewModel.film.observe(this, object : Observer<Film> {
            override fun onChanged(film: Film?) {
                if (film != null) {
                    displayer.display(film)
                }
            }
        })
    }

    companion object {

        @JvmField
        val EXTRA_FILM_ID = BuildConfig.APPLICATION_ID + ".EXTRA_FILM_ID"
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
