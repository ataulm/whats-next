package com.ataulm.whatsnext.search

sealed class SignInUiModel {
    object SignedIn : SignInUiModel()
    object TryingToSignIn : SignInUiModel()
    data class RequiresSignIn(
            val onClickSignIn: (String, String) -> Unit,
            val onClickRegister: () -> Unit,
            val errorMessage: String?
    ) : SignInUiModel()
}
