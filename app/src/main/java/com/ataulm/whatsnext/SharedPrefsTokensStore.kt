package com.ataulm.whatsnext

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsTokensStore private constructor(private val preferences: SharedPreferences) :
    TokensStore {

    override fun store(token: Token) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN_VALUE, token.accessToken)
            .putString(KEY_REFRESH_TOKEN_VALUE, token.refreshToken)
            .apply()
    }

    override fun clear() {
        preferences.edit()
            .remove(KEY_ACCESS_TOKEN_VALUE)
            .remove(KEY_REFRESH_TOKEN_VALUE)
            .apply()
    }

    override fun userIsSignedIn() = token != null

    override val token: Token?
        get() {
            if (preferences.contains(KEY_ACCESS_TOKEN_VALUE) && preferences.contains(
                    KEY_REFRESH_TOKEN_VALUE
                )
            ) {
                val accessToken = preferences.getString(KEY_ACCESS_TOKEN_VALUE, "")!!
                val refreshToken = preferences.getString(KEY_REFRESH_TOKEN_VALUE, "")!!
                return Token(accessToken, refreshToken)
            }
            return null
        }

    companion object {

        @JvmStatic
        private val FILE_NAME = BuildConfig.APPLICATION_ID + ".tokens_store"

        @JvmStatic
        private val KEY_ACCESS_TOKEN_VALUE = "access.value"

        @JvmStatic
        private val KEY_REFRESH_TOKEN_VALUE = "refresh.value"

        @JvmStatic
        fun create(context: Context): SharedPrefsTokensStore {
            val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            return SharedPrefsTokensStore(preferences)
        }
    }
}
