package com.ataulm.whatsnext.signin

import androidx.annotation.StringRes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.core.MainContext
import com.ataulm.whatsnext.nav.NavigateToSearch
import com.ataulm.whatsnext.signin.SignInController.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@Composable
fun SignInRoute(
    navigateToSearch: NavigateToSearch,
    controller: SignInController = hiltViewModel()
) {
    val uiState: UiState by controller.uiState.collectAsStateWithLifecycle()
    SignInScreen()
}

@Composable
fun SignInScreen(

) {
    Text("Sign in screen")
}

@HiltViewModel
class SignInController @Inject constructor(
    private val letterboxdRepository: LetterboxdRepository,
    @MainContext private val mainContext: CoroutineContext
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState(onClickLogin = ::login))
    val uiState = _uiState.asStateFlow()

    private fun login(loginParams: LoginParams) {
        _uiState.update { it.copy(isLoading = true, errorMessageRes = null) }
        viewModelScope.launch(mainContext) {
            try {
                letterboxdRepository.login(loginParams.username, loginParams.password)
                loginParams.navigateToSearch()
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = R.string.something_went_wrong
                    )
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        @StringRes val errorMessageRes: Int? = null,
        val onClickLogin: (LoginParams) -> Unit
    )
}

data class LoginParams(
    val username: String,
    val password: String,
    val navigateToSearch: NavigateToSearch
)
