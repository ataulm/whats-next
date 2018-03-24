package com.ataulm.whatsnext.film

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.WhatsNextService

class FilmViewModelProvider(val service: WhatsNextService, val filmId: String) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val film = FilmLiveData(service, filmId)
        return FilmViewModel(film) as T
    }
}
