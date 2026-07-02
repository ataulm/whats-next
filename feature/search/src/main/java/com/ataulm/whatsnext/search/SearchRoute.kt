package com.ataulm.whatsnext.search

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14171C)) // Deep dark background matching movie theme
    ) {
        // 1. Main body content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp), // Avoid overlapping with bottom search bar
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Search screen",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 2. Bottom section containing the Search Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Search films, directors, or cast...",
                            color = Color(0xFF5D6370),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFF242932),
                    textColor = Color.White,
                    cursorColor = Color(0xFFFF9F54),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // Search logic
                    }
                )
            )
        }
    }
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

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        uiState = SearchController.UiState()
    )
}
