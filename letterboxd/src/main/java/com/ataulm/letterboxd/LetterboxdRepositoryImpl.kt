package com.ataulm.letterboxd

import javax.inject.Inject

@LetterboxdScope
internal class LetterboxdRepositoryImpl @Inject constructor(
    letterboxdApi: LetterboxdApi,
    private val authRepository: AuthRepository
) : LetterboxdRepository, LetterboxdApi by letterboxdApi {

    override suspend fun login(username: String, password: String) {
        authRepository.login(username, password)
    }

    override fun hasUserAccessToken(): Boolean {
        return authRepository.getUserAccessToken() != null
    }
}
