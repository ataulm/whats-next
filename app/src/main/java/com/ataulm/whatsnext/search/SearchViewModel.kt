package com.ataulm.whatsnext.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ataulm.support.Event
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.IsSignedInUseCase
import com.ataulm.whatsnext.account.SignInUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class SearchViewModel(
    isSignedIn: IsSignedInUseCase,
    private val signIn: SignInUseCase,
    private val repository: WhatsNextRepository
) : ViewModel() {

    private val _films = MutableLiveData<List<FilmSummary>>()
    val films: LiveData<List<FilmSummary>> = _films

    private val _signInUiModel = MutableLiveData<SignInUiModel>()
    val signInUiModel: LiveData<SignInUiModel> = _signInUiModel

    private val _navigationEvents = MutableLiveData<Event<FilmSummary>>()
    val navigationEvents: LiveData<Event<FilmSummary>> = _navigationEvents

    init {
        if (isSignedIn()) {
            _signInUiModel.value = SignInUiModel.SignedIn
        } else {
            _signInUiModel.value = SignInUiModel.RequiresSignIn(
                onClickSignIn = { username, password -> onClickSignIn(username, password) },
                onClickRegister = { onClickRegister() },
                errorMessage = null
            )
        }
    }

    private fun onClickSignIn(username: String, password: String) {
        _signInUiModel.value = SignInUiModel.TryingToSignIn
        viewModelScope.launch {
            try {
                signIn(username, password)
                _signInUiModel.value = SignInUiModel.SignedIn
            } catch (e: Exception) {
                _signInUiModel.value = SignInUiModel.RequiresSignIn(
                    onClickSignIn = { username, password -> onClickSignIn(username, password) },
                    onClickRegister = { onClickRegister() },
                    // TODO: we can give a better error message based on the exception
                    errorMessage = "oh, something went wrong."
                )
            }
        }
    }

    private fun onClickRegister() {
        // TODO: send event which will tell the View to open a browser to letterboxd.com
    }

    fun onSearch(searchTerm: String) {
        viewModelScope.launch {
            val results = repository.search(searchTerm)
            _films.value = results
        }
    }

    fun onClick(filmSummary: FilmSummary) {
        _navigationEvents.value = Event(filmSummary)
    }

    fun pagedPopularFilms(): Flow<PagingData<FilmSummary>> {
        val popularFilmsPagingSource = repository.popularFilmsThisWeek()
        val pager = Pager(config = PagingConfig(pageSize = 9)) { popularFilmsPagingSource }
        return pager.flow
    }
}

internal class SearchViewModelFactory(
    private val isSignedInUseCase: IsSignedInUseCase,
    private val signInUseCase: SignInUseCase,
    private val repository: WhatsNextRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = SearchViewModel(
        isSignedInUseCase,
        signInUseCase,
        repository
    ) as T
}
