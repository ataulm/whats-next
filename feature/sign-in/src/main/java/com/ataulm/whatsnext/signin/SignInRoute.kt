package com.ataulm.whatsnext.signin

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
                DevicesIcon(modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TicketLogo(modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(12.dp))
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
                                        uiState.onClickLogin(LoginParams(email, password, navigateToSearch))
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
                                color = if (isFormValid) Color(0xFF1C0D02) else Color(0xFF1C0D02).copy(alpha = 0.6f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // OR Separator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF2C313C),
                            thickness = 1.dp
                        )
                        Text(
                            text = "OR",
                            color = Color(0xFF5D6370),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF2C313C),
                            thickness = 1.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Social Sign In
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Apple Button
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF242932))
                                .clickable {
                                    Toast.makeText(context, "Apple sign in is not supported", Toast.LENGTH_SHORT).show()
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SpeakerIcon(modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Apple",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Google Button
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF242932))
                                .clickable {
                                    Toast.makeText(context, "Google sign in is not supported", Toast.LENGTH_SHORT).show()
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlobeIcon(modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Google",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Create Account Link
                    Text(
                        text = "Create Account",
                        color = Color(0xFF00D37B),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Create account is not supported", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Continue as guest
            Row(
                modifier = Modifier
                    .clickable { navigateToSearch() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Continue as guest",
                    color = Color(0xFF8B949E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "→",
                    color = Color(0xFF8B949E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Links
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRIVACY",
                    color = Color(0xFF5D6370),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier
                        .clickable {
                            Toast.makeText(context, "Privacy Policy not available", Toast.LENGTH_SHORT).show()
                        }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "TERMS",
                    color = Color(0xFF5D6370),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier
                        .clickable {
                            Toast.makeText(context, "Terms of Service not available", Toast.LENGTH_SHORT).show()
                        }
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "SUPPORT",
                    color = Color(0xFF5D6370),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier
                        .clickable {
                            Toast.makeText(context, "Support not available", Toast.LENGTH_SHORT).show()
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun DevicesIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val color = Color.White
        val w = size.width
        val h = size.height
        val stroke = 1.5.dp.toPx()
        
        // Draw monitor screen outline
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, h * 0.15f),
            size = Size(w * 0.75f, h * 0.55f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(width = stroke)
        )
        
        // Monitor stand
        drawLine(
            color = color,
            start = Offset(w * 0.37f, h * 0.7f),
            end = Offset(w * 0.37f, h * 0.85f),
            strokeWidth = stroke
        )
        drawLine(
            color = color,
            start = Offset(w * 0.2f, h * 0.85f),
            end = Offset(w * 0.55f, h * 0.85f),
            strokeWidth = stroke
        )
        
        // Draw mobile phone outline overlapping the screen on bottom right
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.6f, h * 0.35f),
            size = Size(w * 0.35f, h * 0.55f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(width = stroke)
        )
        // Background fill for the phone to mask monitor
        drawRoundRect(
            color = Color(0xFF0C0F12),
            topLeft = Offset(w * 0.6f + stroke/2, h * 0.35f + stroke/2),
            size = Size(w * 0.35f - stroke, h * 0.55f - stroke),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        // Redraw phone stroke
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.6f, h * 0.35f),
            size = Size(w * 0.35f, h * 0.55f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(width = stroke)
        )
    }
}

@Composable
fun TicketLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val ticketColor = Color(0xFFFF9F54)
        
        val path = Path().apply {
            moveTo(8.dp.toPx(), 0f)
            lineTo(width - 8.dp.toPx(), 0f)
            quadraticTo(width, 0f, width, 8.dp.toPx())
            
            lineTo(width, height * 0.35f)
            arcTo(
                rect = Rect(width - 8.dp.toPx(), height * 0.35f, width + 8.dp.toPx(), height * 0.65f),
                startAngleDegrees = 270f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(width, height - 8.dp.toPx())
            quadraticTo(width, height, width - 8.dp.toPx(), height)
            
            lineTo(8.dp.toPx(), height)
            quadraticTo(0f, height, 0f, height - 8.dp.toPx())
            
            lineTo(0f, height * 0.65f)
            arcTo(
                rect = Rect(-8.dp.toPx(), height * 0.35f, 8.dp.toPx(), height * 0.65f),
                startAngleDegrees = 90f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(0f, 8.dp.toPx())
            quadraticTo(0f, 0f, 8.dp.toPx(), 0f)
            close()
        }
        
        drawPath(path = path, color = ticketColor)
        
        val darkColor = Color(0xFF1C0D02)
        val starPath = Path().apply {
            val cx = width * 0.62f
            val cy = height * 0.5f
            val rOuter = width * 0.22f
            val rInner = width * 0.08f
            for (i in 0 until 10) {
                val angle = i * Math.PI / 5
                val r = if (i % 2 == 0) rOuter else rInner
                val x = cx + r * Math.sin(angle).toFloat()
                val y = cy - r * Math.cos(angle).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(path = starPath, color = darkColor)
        
        val dotRadius = height * 0.04f
        val dotX = width * 0.28f
        val dotSpacing = height * 0.16f
        var currentY = height * 0.15f
        while (currentY < height * 0.9f) {
            drawCircle(color = darkColor, radius = dotRadius, center = Offset(dotX, currentY))
            currentY += dotSpacing
        }
    }
}

@Composable
fun SpeakerIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val color = Color.White
        val w = size.width
        val h = size.height
        
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.38f)
            lineTo(w * 0.4f, h * 0.38f)
            lineTo(w * 0.7f, h * 0.18f)
            lineTo(w * 0.7f, h * 0.82f)
            lineTo(w * 0.4f, h * 0.62f)
            lineTo(w * 0.15f, h * 0.62f)
            close()
        }
        drawPath(path = path, color = color)
        
        drawArc(
            color = color,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(w * 0.5f, h * 0.28f),
            size = Size(w * 0.35f, h * 0.44f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = color,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(w * 0.4f, h * 0.18f),
            size = Size(w * 0.55f, h * 0.64f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun GlobeIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val color = Color.White
        val strokeWidth = 1.5.dp.toPx()
        
        drawCircle(
            color = color,
            radius = size.minDimension / 2 - strokeWidth,
            style = Stroke(width = strokeWidth)
        )
        
        drawLine(
            color = color,
            start = Offset(size.width / 2, strokeWidth),
            end = Offset(size.width / 2, size.height - strokeWidth),
            strokeWidth = strokeWidth
        )
        
        drawLine(
            color = color,
            start = Offset(strokeWidth, size.height / 2),
            end = Offset(size.width - strokeWidth, size.height / 2),
            strokeWidth = strokeWidth
        )
        
        val path = Path().apply {
            addOval(
                Rect(
                    left = size.width * 0.25f,
                    top = strokeWidth,
                    right = size.width * 0.75f,
                    bottom = size.height - strokeWidth
                )
            )
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = strokeWidth)
        )
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
