package com.ataulm.whatsnext.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.FilmId
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class FilmViewModel(
        @FilmId private val filmId: String,
        private val whatsNextRepository: WhatsNextRepository,
        private val tokensStore: TokensStore
) : ViewModel() {

    private val _filmDetails = MutableLiveData<FilmDetailsUiModel>()
    val filmDetails: LiveData<FilmDetailsUiModel> = _filmDetails

    private var watched = false
    private var liked = false
    private var inWatchlist = false
    private var rating = FilmRating.UNRATED
    private var latestFilmRelationshipJob: Job? = null

    init {
        viewModelScope.launch {
            val filmSummary = whatsNextRepository.filmSummary(filmId)
            _filmDetails.value = FilmDetailsUiModel(filmSummary)
            if (tokensStore.userIsSignedIn()) {
                val filmRelationship = whatsNextRepository.filmRelationship(filmId)
                cacheUpdatedRelationshipThenEmit(filmRelationship)
            }
        }
    }

    private fun cacheUpdatedRelationshipThenEmit(filmRelationship: FilmRelationship) {
        watched = filmRelationship.watched
        liked = filmRelationship.liked
        inWatchlist = filmRelationship.inWatchlist
        rating = filmRelationship.rating

        _filmDetails.value = _filmDetails.value!!.copy(filmRelationship = filmRelationship)
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
            val updatedFilmRelationship = whatsNextRepository.updateFilmRelationship(
                    letterboxdId = filmId,
                    watched = watched,
                    liked = liked,
                    inWatchlist = inWatchlist,
                    rating = rating
            )
            cacheUpdatedRelationshipThenEmit(updatedFilmRelationship)
        }
    }

    data class FilmDetailsUiModel(
            val filmSummary: FilmSummary,
            val filmRelationship: FilmRelationship? = null
    )
}
