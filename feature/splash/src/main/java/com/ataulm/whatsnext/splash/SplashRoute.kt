package com.ataulm.whatsnext.splash

import androidx.lifecycle.ViewModel
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.splash.SplashController.UiState.Loading
import com.ataulm.whatsnext.splash.SplashController.UiState.LoggedIn
import com.ataulm.whatsnext.splash.SplashController.UiState.NotLoggedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashController @Inject constructor(
    letterboxdRepository: LetterboxdRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        if (letterboxdRepository.hasUserAccessToken()) {
            _uiState.update { LoggedIn }
        } else {
            _uiState.update { NotLoggedIn }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data object NotLoggedIn : UiState()
        data object LoggedIn : UiState()
    }
}
