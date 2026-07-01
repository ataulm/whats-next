package com.ataulm.whatsnext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.nav.NavRoute
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashController: SplashController by viewModels()
    private var isAppReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isAppReady }

        setContent {
            val uiState by splashController.uiState.collectAsStateWithLifecycle()

            MaterialTheme {
                if (uiState !is SplashController.UiState.Loading) {
                    val startDestination = if (uiState is SplashController.UiState.LoggedIn) {
                        NavRoute.SEARCH
                    } else {
                        NavRoute.SIGN_IN
                    }
                    App(
                        startDestination = startDestination,
                        onAppReady = { isAppReady = true },
                    )
                }
            }
        }
    }
}

@HiltViewModel
class SplashController @Inject constructor(
    letterboxdRepository: LetterboxdRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        if (letterboxdRepository.hasUserAccessToken()) {
            _uiState.update { UiState.LoggedIn }
        } else {
            _uiState.update { UiState.NotLoggedIn }
        }
    }

    sealed class UiState {
        data object Loading : UiState()
        data object NotLoggedIn : UiState()
        data object LoggedIn : UiState()
    }
}
