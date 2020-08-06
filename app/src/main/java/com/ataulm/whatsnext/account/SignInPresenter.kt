package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.WhatsNextService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class SignInPresenter(
        private val service: WhatsNextService,
        private val screen: SignInScreen,
        private val callback: Callback
) {

    fun startPresenting() {
        screen.attach(onClickSignInCallback)
    }

    private val onClickSignInCallback = SignInScreen.Callback { username, password ->
        // hmm no, usually we'd use viewModelScope, but what about when we're doing this from a rando presenter?
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                screen.showLoading()
            }
            withContext(Dispatchers.IO) {
                // how do we do error handling in coroutines?
                try {
                    val token = service.login(username, password)
                    withContext(Dispatchers.Main) {
                        callback.onTokenReceieved(username, token)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        screen.showErrorSigningIn()
                    }
                }
            }
        }
    }

    fun stopPresenting() {
        // TODO: cancel coroutines
    }

    interface Callback {
        fun onTokenReceieved(username: String, token: Token)
    }
}
