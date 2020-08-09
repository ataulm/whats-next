package com.ataulm.whatsnext.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.FilmRating
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.di.FilmId
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class FilmViewModel(
        @FilmId private val filmId: String,
        private val whatsNextRepository: WhatsNextRepository
) : ViewModel() {

    private val _film = MutableLiveData<Film>()
    val film: LiveData<Film> = _film

    private var watched = false
    private var liked = false
    private var inWatchlist = false
    private var rating = FilmRating.UNRATED
    private var latestFilmRelationshipJob: Job? = null

    init {
        viewModelScope.launch {
            val film = whatsNextRepository.film(filmId)
            cacheUpdatedRelationshipThenEmit(film)
        }
    }

    private fun cacheUpdatedRelationshipThenEmit(film: Film) {
        watched = film.relationship.watched
        liked = film.relationship.liked
        inWatchlist = film.relationship.inWatchlist
        rating = film.relationship.rating

        _film.value = film
    }

    fun onClickWatched(watched: Boolean) {
        this.watched = watched
        updateFilmRelationshipRemotely()
    }

    fun onClickLiked(liked: Boolean) {
        this.liked = liked
        updateFilmRelationshipRemotely()
    }

    fun onClickInWatchlist(inWatchlist: Boolean) {
        this.inWatchlist = inWatchlist
        updateFilmRelationshipRemotely()
    }

    fun onClickRating(rating: Float) {
        this.rating = FilmRating.fromFloat(rating)
        updateFilmRelationshipRemotely()
    }

    private fun updateFilmRelationshipRemotely() {
        // - these calls should always include the latest values that the _user_ wants
        // so we'll cancel the previous request to avoid accidentally overriding the
        // latest user value with what the server thinks.
        // - this means if the user updates multiple properties, we're not reliant on
        // the requests being processed by the server in the queue.
        latestFilmRelationshipJob?.cancel()

        latestFilmRelationshipJob = viewModelScope.launch {
            val film = whatsNextRepository.updateFilmRelationship(
                    letterboxdId = filmId,
                    watched = watched,
                    liked = liked,
                    inWatchlist = inWatchlist,
                    rating = rating
            )
            cacheUpdatedRelationshipThenEmit(film)
        }
    }
}
