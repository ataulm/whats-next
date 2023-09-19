package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.api.auth.AuthRepository
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(): Boolean {
        return authRepository.getUserAccessToken() != null
    }
}
