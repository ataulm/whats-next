package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.TokensStore

class InMemoryTokensStore(
    private var _userToken: Token? = null,
    private var _clientToken: String? = null
) : TokensStore {

    override fun storeUserToken(token: Token) {
        _userToken = token
    }

    override val userToken: Token?
        get() = _userToken

    override fun userIsSignedIn(): Boolean {
        return _userToken != null
    }

    override fun clearUserToken() {
        _userToken = null
    }

    override fun storeClientToken(accessToken: String) {
        _clientToken = accessToken
    }

    override val clientToken: String?
        get() = _clientToken
}