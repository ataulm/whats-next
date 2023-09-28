package com.ataulm.whatsnext

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ataulm.whatsnext.nav.NavRoute
import com.ataulm.whatsnext.nav.NavigateToSearch
import com.ataulm.whatsnext.nav.NavigateToSignIn
import com.ataulm.whatsnext.splash.SplashRoute

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.SPLASH,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = NavRoute.SPLASH) {
                // Remove the SPLASH screen from the back stack
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(route = NavRoute.SPLASH, inclusive = true)
                    .build()
                SplashRoute(
                    navigateToSignIn = NavigateToSignIn {
                        navController.navigate(NavRoute.SIGN_IN, navOptions)
                    },
                    navigateToSearch = NavigateToSearch {
                        navController.navigate(NavRoute.SEARCH, navOptions)
                    }
                )
            }
            composable(route = NavRoute.SIGN_IN) {
                Text("sign in screen")
            }
            composable(route = NavRoute.SEARCH) {
                Text("search screen")
            }
            composable(route = NavRoute.FILM_DETAIL) {
                Text("film detail screen")
            }
            composable(route = NavRoute.WATCH_LIST) {
                Text("watch list screen")
            }
        }
    }
}
