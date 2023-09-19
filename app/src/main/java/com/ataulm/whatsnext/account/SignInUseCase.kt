package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.api.auth.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(username: String, password: String) {
        repository.login(username, password)
    }
}
