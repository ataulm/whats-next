package com.ataulm.whatsnext

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ataulm.whatsnext.nav.NavRoute
import com.ataulm.whatsnext.nav.NavigateToSearch
import com.ataulm.whatsnext.search.SearchRoute
import com.ataulm.whatsnext.signin.SignInRoute

@Composable
fun App(
    startDestination: String,
    onAppReady: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(route = NavRoute.SIGN_IN) {
                LaunchedEffect(Unit) {
                    onAppReady()
                }
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(route = NavRoute.SIGN_IN, inclusive = true)
                    .build()
                SignInRoute(
                    navigateToSearch = NavigateToSearch {
                        navController.navigate(NavRoute.SEARCH, navOptions)
                    },
                )
            }
            composable(route = NavRoute.SEARCH) {
                LaunchedEffect(Unit) {
                    onAppReady()
                }
                SearchRoute()
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
