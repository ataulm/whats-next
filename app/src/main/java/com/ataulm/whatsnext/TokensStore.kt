package com.ataulm.whatsnext

interface TokensStore {

    fun store(token: Token)

    val token: Token?
    fun userIsSignedIn(): Boolean
    fun clear()
}