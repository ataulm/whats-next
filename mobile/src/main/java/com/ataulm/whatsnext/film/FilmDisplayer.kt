package com.ataulm.whatsnext.film

import android.widget.Button
import android.widget.TextView
import com.ataulm.whatsnext.Film

internal class FilmDisplayer(
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
        detailsInfoWidget.bind(film.summary)
        watchStatusTextView.text = if (film.relationship.watched) "watched" else "not watched"
    }

    internal interface Callback {

        fun onClickMarkAsWatched()

        fun onClickMarkAsNotWatched()
    }
}
