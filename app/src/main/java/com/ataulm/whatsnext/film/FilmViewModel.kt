package com.ataulm.whatsnext.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.UserIsSignedInUseCase
import com.ataulm.whatsnext.model.Film
import com.ataulm.whatsnext.model.FilmRating
import com.ataulm.whatsnext.model.FilmRelationship
import com.ataulm.whatsnext.model.FilmStats
import com.ataulm.whatsnext.model.FilmSummary
import com.ataulm.whatsnext.model.Images
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FilmViewModel(
    private val filmSummary: FilmSummary,
    private val whatsNextRepository: WhatsNextRepository,
    private val isSignedIn: UserIsSignedInUseCase
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
            _filmDetails.value = FilmDetailsUiModel(filmSummary)
            val filmId = filmSummary.ids.letterboxd
            _filmDetails.value =
                whatsNextRepository.film(filmId).let { _filmDetails.value!!.copy(film = it) }
            _filmDetails.value = whatsNextRepository.filmStats(filmId)
                .let { _filmDetails.value!!.copy(filmStats = it) }
            if (isSignedIn()) {
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
                letterboxdId = filmSummary.ids.letterboxd,
                watched = watched,
                liked = liked,
                inWatchlist = inWatchlist,
                rating = rating
            )
            cacheUpdatedRelationshipThenEmit(updatedFilmRelationship)
        }
    }

    data class FilmDetailsUiModel(
        val title: String,
        val releaseYear: String?,
        val poster: Images,
        val directors: List<String>,
        val filmSummary: FilmSummary,
        val film: Film? = null,
        val filmRelationship: FilmRelationship? = null,
        val filmStats: FilmStats? = null
    ) {
        companion object {
            operator fun invoke(filmSummary: FilmSummary) = FilmDetailsUiModel(
                title = filmSummary.name,
                releaseYear = filmSummary.year,
                poster = filmSummary.poster,
                directors = filmSummary.directors.map { it.name },
                filmSummary = filmSummary
            )
        }
    }
}
