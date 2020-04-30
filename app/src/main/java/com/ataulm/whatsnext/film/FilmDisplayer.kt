package com.ataulm.whatsnext.film

import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import com.ataulm.whatsnext.Film
import com.bumptech.glide.Glide

internal class FilmDisplayer(
        private val titleTextView: TextView,
        private val watchedCheckBox: CheckBox,
        private val likeCheckBox: CheckBox,
        private val ratingBar: RatingBar
) {

    private lateinit var callback: Callback

    fun attach(callback: Callback) {
        this.callback = callback
        // TODO: haven't got this in the callback yet
        likeCheckBox.isEnabled = false
    }

    fun display(film: Film) {
        titleTextView.text = if (film.summary.year != null) {
            "${film.summary.name} (${film.summary.year})"
        } else {
            film.summary.name
        }
        likeCheckBox.isChecked = film.relationship.liked
        watchedCheckBox.isChecked = film.relationship.watched
        ratingBar.rating = film.relationship.rating.toFloat()
    }

    internal interface Callback {

        fun onClickMarkAsWatched()

        fun onClickMarkAsNotWatched()
    }
}
