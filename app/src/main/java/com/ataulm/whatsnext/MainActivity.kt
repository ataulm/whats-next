package com.ataulm.whatsnext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataulm.whatsnext.nav.NavRoute
import com.ataulm.whatsnext.splash.SplashController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashController: SplashController by viewModels()
    private var isAppReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

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
