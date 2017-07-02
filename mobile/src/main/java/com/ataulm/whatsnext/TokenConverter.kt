package com.ataulm.whatsnext

import java.util.concurrent.TimeUnit

internal class TokenConverter(private val clock: Clock) {

    fun convert(apiAuthResponse: ApiAuthResponse): Token {
        val expiryTime = clock.currentTimeMillis + TimeUnit.SECONDS.toMillis(apiAuthResponse.secondsUntilExpiry)
        return Token(apiAuthResponse.accessToken, apiAuthResponse.refreshToken, expiryTime)
    }
}
