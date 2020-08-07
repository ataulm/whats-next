package com.ataulm.whatsnext.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.di.FilmId
import kotlinx.coroutines.launch

internal class FilmViewModel(@FilmId filmId: String, whatsNextService: WhatsNextService) : ViewModel() {

    private val _film = MutableLiveData<Film>()
    val film: LiveData<Film> = _film

    init {
        viewModelScope.launch {
            val film = whatsNextService.film(filmId)
            _film.value = film
        }
    }
}
