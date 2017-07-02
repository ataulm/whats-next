package com.ataulm.whatsnext

internal data class Token(val accessToken: String, val refreshToken: String, val expiryMillisSinceEpoch: Long)
