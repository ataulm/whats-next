package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.WhatsNextService
import kotlinx.coroutines.*

internal class SignInPresenter(
        private val service: WhatsNextService,
        private val screen: SignInScreen,
        private val callback: Callback
) {

    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val onClickSignInCallback = SignInScreen.Callback { username, password ->
        presenterScope.launch {
            screen.showLoading()
            try {
                val token = service.login(username, password)
                callback.onTokenReceieved(username, token)
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
        fun onTokenReceieved(username: String, token: Token)
    }
}
