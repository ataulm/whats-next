package com.ataulm.whatsnext.film

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ataulm.whatsnext.Film
import com.bumptech.glide.Glide

internal class FilmDisplayer(
        private val backdropImageView: ImageView,
        private val detailsInfoWidget: FilmDetailsInfoWidget,
        private val castPeopleWidget: PeopleWidget,
        private val crewPeopleWidget: PeopleWidget,
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
        castPeopleWidget.bind(castFrom(film))
        crewPeopleWidget.bind(crewFrom(film))

        watchStatusTextView.text = if (film.relationship.watched) "watched" else "not watched"
    }

    // TODO: should be done at presenter level
    private fun castFrom(film: Film): List<PeopleWidget.PersonViewModel> {
        return film.summary.cast.map { PeopleWidget.PersonViewModel(it.person.name, it.character) }
    }

    // TODO: should be done at presenter level
    private fun crewFrom(film: Film): List<PeopleWidget.PersonViewModel> {
        return film.summary.crew.map { PeopleWidget.PersonViewModel(it.person.name, it.type) }
    }

    internal interface Callback {

        fun onClickMarkAsWatched()

        fun onClickMarkAsNotWatched()
    }
}
