package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextRepository

internal class SignInUseCase(
        private val repository: WhatsNextRepository,
        private val tokensStore: TokensStore
) {

    suspend operator fun invoke(username: String, password: String) {
        val token = repository.login(username, password)
        tokensStore.store(token)
    }
}
