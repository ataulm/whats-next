package com.ataulm.whatsnext.film

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ataulm.whatsnext.Film
import com.bumptech.glide.Glide

internal class FilmDisplayer(
        private val backdropImageView: ImageView,
        private val detailsInfoWidget: FilmDetailsInfoWidget,
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
        Glide.with(backdropImageView.context)
                .load(film.summary.backdrop.bestFor(backdropImageView.width)?.url)
                .into(backdropImageView)
        detailsInfoWidget.bind(film.summary)
        watchStatusTextView.text = if (film.relationship.watched) "watched" else "not watched"
    }

    internal interface Callback {

        fun onClickMarkAsWatched()

        fun onClickMarkAsNotWatched()
    }
}
