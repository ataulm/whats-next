package com.ataulm.whatsnext.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.di.FilmId
import javax.inject.Inject

internal class FilmViewModelProvider @Inject constructor(
        @FilmId val filmId: String,
        private val repository: WhatsNextRepository,
        private val tokensStore: TokensStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmViewModel(filmId, repository, tokensStore) as T
    }
}
