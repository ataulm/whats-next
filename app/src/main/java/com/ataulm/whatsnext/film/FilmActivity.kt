package com.ataulm.whatsnext.film

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.ataulm.support.Toaster
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.activity_film.*

class FilmActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!intent.hasExtra(EXTRA_FILM_ID)) {
            throw IllegalStateException("how you open FilmActivity without a film id?")
        }

        setContentView(R.layout.activity_film)
        val filmId = intent.getStringExtra(EXTRA_FILM_ID)
        val viewModelProvider = ViewModelProviders.of(this, FilmViewModelProvider(whatsNextService(), filmId))
        val viewModel = viewModelProvider.get(FilmViewModel::class.java)

        val displayer = FilmDisplayer(
                film_details_backdrop,
                film_details_info,
                film_details_people_cast,
                film_details_people_crew,
                film_text_watch_status,
                film_button_mark_watched,
                film_button_mark_not_watched
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
