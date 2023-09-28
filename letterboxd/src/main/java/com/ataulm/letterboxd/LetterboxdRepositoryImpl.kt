package com.ataulm.letterboxd

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LetterboxdRepositoryImpl @Inject constructor(
    letterboxdApi: LetterboxdApi,
    private val authRepository: AuthRepository
) : LetterboxdRepository, LetterboxdApi by letterboxdApi {

    override suspend fun login(username: String, password: String) {
        authRepository.login(username, password)
    }

    override fun logout() {
        authRepository.clearUserTokens()
    }

    override fun hasUserAccessToken(): Boolean {
        return authRepository.getUserAccessToken() != null
    }
}
