package com.ataulm.whatsnext.search

import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.core.MainContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@Composable
fun SearchRoute(
    controller: SearchController = hiltViewModel()
) {
    val uiState: SearchController.UiState by controller.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        uiState = uiState,
    )
}

@Composable
fun SearchScreen(
    uiState: SearchController.UiState,
) {
    Text("Search screen")
}

@HiltViewModel
class SearchController @Inject constructor(
    private val letterboxdRepository: LetterboxdRepository,
    @MainContext private val mainContext: CoroutineContext
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val isLoading: Boolean = false,
        @StringRes val errorMessageRes: Int? = null
    )
}
