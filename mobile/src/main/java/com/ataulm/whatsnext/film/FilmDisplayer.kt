package com.ataulm.whatsnext.film

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ataulm.whatsnext.Film
import com.bumptech.glide.Glide

internal class FilmDisplayer(
        private val posterImageView: ImageView,
        private val titleTextView: TextView,
        private val watchStatusTextView: TextView,
        private val markAsWatchedButton: Button,
        private val markAsNotWatchedButton: Button
) {

    fun attach(callback: Callback) {
        markAsWatchedButton.setOnClickListener {
            callback.onClickMarkAsWatched()
        }
        markAsNotWatchedButton.setOnClickListener {
            callback.onClickMarkAsNotWatched()
        }
    }

    fun detachCallback() {
        markAsWatchedButton.setOnClickListener(null)
        markAsNotWatchedButton.setOnClickListener(null)
    }

    fun display(film: Film) {
        titleTextView.text = film.summary.name
        watchStatusTextView.text = if (film.relationship.watched) "watched" else "not watched"

        Glide.with(posterImageView.context)
                .load(film.summary.poster.bestFor(posterImageView.width, posterImageView.height)?.url)
                .into(posterImageView)
    }

    internal interface Callback {

        fun onClickMarkAsWatched()

        fun onClickMarkAsNotWatched()
    }
}
