package com.ataulm.whatsnext.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.di.FilmId
import javax.inject.Inject

class FilmViewModelProvider @Inject constructor(
        @FilmId val filmId: String,
        private val service: WhatsNextService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val film = FilmLiveData(service, filmId)
        return FilmViewModel(film) as T
    }
}
