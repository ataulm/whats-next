package com.ataulm.whatsnext

interface TokensStore {

    fun storeUserToken(token: Token)

    val userToken: Token?
    fun userIsSignedIn(): Boolean

    fun clearUserToken()

    fun storeClientToken(accessToken: String)

    val clientToken: String?
}