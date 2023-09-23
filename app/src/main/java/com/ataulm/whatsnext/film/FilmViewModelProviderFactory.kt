package com.ataulm.whatsnext.film

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.UserIsSignedInUseCase
import com.ataulm.whatsnext.model.FilmSummary
import javax.inject.Inject

class FilmViewModelProviderFactory @Inject constructor(
    private val filmSummary: FilmSummary,
    private val repository: WhatsNextRepository,
    private val userIsSignedInUseCase: UserIsSignedInUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilmViewModel(filmSummary, repository, userIsSignedInUseCase) as T
    }
}
