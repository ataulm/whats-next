package com.ataulm.whatsnext.letterboxd

interface TokenStore {

    fun store(token: AccessToken)

    fun clear()

    fun token(): AccessToken?
}
