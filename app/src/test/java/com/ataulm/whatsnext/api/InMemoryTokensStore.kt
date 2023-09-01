package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.TokensStore

class InMemoryTokensStore(private var _token: Token? = null) : TokensStore {

    override fun store(token: Token) {
        _token = token
    }

    override val token: Token?
        get() = _token

    override fun userIsSignedIn(): Boolean {
        return _token != null
    }

    override fun clear() {
        _token = null
    }
}