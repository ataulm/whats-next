package com.ataulm.whatsnext.signin

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    SignInScreen(
        uiState = uiState,
        navigateToSearch = navigateToSearch
    )
}

@Composable
fun SignInScreen(
    uiState: UiState,
    navigateToSearch: NavigateToSearch
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isFormValid = email.isNotBlank() && password.isNotBlank()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 1. Background image
        Image(
            painter = painterResource(id = R.drawable.cinema_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Translucent dark overlay with gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // 3. Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Film Tracker",
                    color = Color(0xFFFF9F54),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Track films you've watched.\nSave those you want to see.",
                color = Color(0xFFE6EDF3),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Central Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF1E2229).copy(alpha = 0.85f))
                    .border(1.dp, Color(0xFF2C313C), RoundedCornerShape(28.dp))
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Show error message if exists
                    uiState.errorMessageRes?.let {
                        Text(
                            text = stringResource(it),
                            color = Color(0xFFFF6B6B),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Username Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "USERNAME",
                            color = Color(0xFF888E9E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("joeBloggs123", color = Color(0xFF5D6370)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFF242932),
                                textColor = Color.White,
                                cursorColor = Color(0xFFFF9F54),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "PASSWORD",
                            color = Color(0xFF888E9E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••", color = Color(0xFF5D6370)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0xFF242932),
                                textColor = Color.White,
                                cursorColor = Color(0xFFFF9F54),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    if (isFormValid && !uiState.isLoading) {
                                        uiState.onClickLogin(
                                            LoginParams(
                                                email,
                                                password,
                                                navigateToSearch
                                            )
                                        )
                                    }
                                }
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Sign In Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(
                                if (isFormValid && !uiState.isLoading) {
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFFFAD66), Color(0xFFFF7A00))
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFFAD66).copy(alpha = 0.5f),
                                            Color(0xFFFF7A00).copy(alpha = 0.5f)
                                        )
                                    )
                                }
                            )
                            .clickable(enabled = isFormValid && !uiState.isLoading) {
                                focusManager.clearFocus()
                                uiState.onClickLogin(LoginParams(email, password, navigateToSearch))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color(0xFF1C0D02),
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "Sign In",
                                color = if (isFormValid) Color(0xFF1C0D02) else Color(0xFF1C0D02).copy(
                                    alpha = 0.6f
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
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
