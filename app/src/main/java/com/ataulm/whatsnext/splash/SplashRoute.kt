package com.ataulm.whatsnext.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.nav.NavigateToSearch
import com.ataulm.whatsnext.nav.NavigateToSignIn
import com.ataulm.whatsnext.splash.SplashController.UiState
import com.ataulm.whatsnext.splash.SplashController.UiState.Loading
import com.ataulm.whatsnext.splash.SplashController.UiState.LoggedIn
import com.ataulm.whatsnext.splash.SplashController.UiState.NotLoggedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// TODO: move to its own module
@Composable
fun SplashRoute(
    navigateToSignIn: NavigateToSignIn,
    navigateToSearch: NavigateToSearch,
    modifier: Modifier = Modifier,
    controller: SplashController = hiltViewModel()
) {
    val uiState: UiState by controller.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            Loading -> {} // no-op - this is the default state
            LoggedIn -> navigateToSearch()
            NotLoggedIn -> navigateToSignIn()
        }
    }
    SplashScreen(modifier = modifier)
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("splashysplashy")
    }
}

@HiltViewModel
class SplashController @Inject constructor(
    letterboxdRepository: LetterboxdRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(Loading)
    val uiState = _uiState.asStateFlow()
        .apply {
            if (BuildConfig.DEBUG) {
                onEach { delay(750) }
            }
        }

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
