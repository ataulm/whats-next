package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.AuthRepository
import javax.inject.Inject

class UserIsSignedInUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(): Boolean {
        return authRepository.getUserAccessToken() != null
    }
}
