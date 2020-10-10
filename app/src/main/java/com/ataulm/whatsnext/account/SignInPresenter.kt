package com.ataulm.whatsnext.account

import kotlinx.coroutines.*

internal class SignInPresenter(
        private val signIn: SignInUseCase,
        private val screen: SignInScreen,
        private val callback: Callback
) {

    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val onClickSignInCallback = SignInScreen.Callback { username, password ->
        presenterScope.launch {
            screen.showLoading()
            try {
                signIn(username, password)
                callback.onSignInCompleted()
            } catch (e: Exception) {
                screen.showErrorSigningIn()
            }
        }
    }

    init {
        screen.attach(onClickSignInCallback)
    }

    fun onDestroy() {
        presenterScope.cancel()
    }

    interface Callback {
        fun onSignInCompleted()
    }
}
