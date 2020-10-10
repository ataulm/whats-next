package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.TokensStore

internal class IsSignedInUseCase(private val tokensStore: TokensStore) {

    operator fun invoke(): Boolean {
        return tokensStore.userIsSignedIn()
    }
}
