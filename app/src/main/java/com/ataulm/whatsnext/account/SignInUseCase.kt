package com.ataulm.whatsnext.account

import com.ataulm.letterboxd.LetterboxdRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: LetterboxdRepository
) {

    suspend operator fun invoke(username: String, password: String) {
        repository.login(username, password)
    }
}
