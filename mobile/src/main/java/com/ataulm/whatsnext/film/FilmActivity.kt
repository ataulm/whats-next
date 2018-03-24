package com.ataulm.whatsnext.film

import android.os.Bundle
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.activity_film.*

class FilmActivity : BaseActivity() {

    private lateinit var presenter: FilmPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!intent.hasExtra(EXTRA_FILM_ID)) {
            throw IllegalStateException("how you open FilmActivity without a film id?")
        }

        setContentView(R.layout.activity_film)

        val displayer = FilmDisplayer(
                film_details_backdrop,
                film_details_info,
                film_details_people_cast,
                film_details_people_crew,
                film_text_watch_status,
                film_button_mark_watched,
                film_button_mark_not_watched
        )
        val filmId = intent.getStringExtra(EXTRA_FILM_ID)
        presenter = FilmPresenter(whatsNextService(), displayer, filmId)
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresenting()
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    companion object {

        @JvmField
        val EXTRA_FILM_ID = BuildConfig.APPLICATION_ID + ".EXTRA_FILM_ID"
    }
}
