package com.ataulm.whatsnext.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextRepository
import javax.inject.Inject

internal class FilmViewModelProviderFactory @Inject constructor(
        private val filmSummary: FilmSummary,
        private val repository: WhatsNextRepository,
        private val tokensStore: TokensStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilmViewModel(filmSummary, repository, tokensStore) as T
    }
}
