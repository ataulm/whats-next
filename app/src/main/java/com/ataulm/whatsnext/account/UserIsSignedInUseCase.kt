package com.ataulm.whatsnext.account

import com.ataulm.letterboxd.LetterboxdRepository
import javax.inject.Inject

class UserIsSignedInUseCase @Inject constructor(private val repository: LetterboxdRepository) {

    operator fun invoke(): Boolean {
        return repository.hasUserAccessToken()
    }
}
