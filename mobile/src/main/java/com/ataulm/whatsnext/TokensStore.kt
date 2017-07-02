package com.ataulm.whatsnext

import android.content.Context
import android.content.SharedPreferences

internal class TokensStore private constructor(private val preferences: SharedPreferences) {

    fun store(token: Token) {
        preferences.edit()
                .putString(KEY_ACCESS_TOKEN_VALUE, token.accessToken)
                .putString(KEY_REFRESH_TOKEN_VALUE, token.refreshToken)
                .putLong(KEY_ACCESS_TOKEN_EXPIRY, token.expiryMillisSinceEpoch)
                .apply()
    }

    val token: Token?
        get() {
            if (preferences.contains(KEY_ACCESS_TOKEN_VALUE) && preferences.contains(KEY_ACCESS_TOKEN_EXPIRY)) {
                val accessToken = preferences.getString(KEY_ACCESS_TOKEN_VALUE, "")
                val refreshToken = preferences.getString(KEY_REFRESH_TOKEN_VALUE, "")
                val expiry = preferences.getLong(KEY_ACCESS_TOKEN_EXPIRY, 0)
                return Token(accessToken!!, refreshToken!!, expiry)
            }
            return null
        }

    companion object {

        @JvmStatic private val FILE_NAME = BuildConfig.APPLICATION_ID + ".tokens_store"
        @JvmStatic private val KEY_ACCESS_TOKEN_VALUE = "access.value"
        @JvmStatic private val KEY_REFRESH_TOKEN_VALUE = "refresh.value"
        @JvmStatic private val KEY_ACCESS_TOKEN_EXPIRY = "access.expiry"

        fun create(context: Context): TokensStore {
            val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            return TokensStore(preferences)
        }

    }

}
