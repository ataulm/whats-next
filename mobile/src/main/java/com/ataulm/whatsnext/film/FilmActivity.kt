package com.ataulm.whatsnext.film

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.R

class FilmActivity : BaseActivity() {

    private lateinit var presenter: FilmPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!intent.hasExtra(EXTRA_FILM_ID)) {
            throw IllegalStateException("how you open FilmActivity without a film id?")
        }

        setContentView(R.layout.activity_film)

        val posterImageView = ButterKnife.findById<ImageView>(this, R.id.film_image_poster)
        val titleTextView = ButterKnife.findById<TextView>(this, R.id.film_text_title)
        val watchStatusTextView = ButterKnife.findById<TextView>(this, R.id.film_text_watch_status)
        val markAsWatchedButton = ButterKnife.findById<Button>(this, R.id.film_button_mark_watched)
        val markAsNotWatchedButton = ButterKnife.findById<Button>(this, R.id.film_button_mark_not_watched)

        val displayer = FilmDisplayer(posterImageView, titleTextView, watchStatusTextView, markAsWatchedButton, markAsNotWatchedButton)
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