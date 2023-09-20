package com.ataulm.whatsnext

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SharedPrefsTokensStore private constructor(
    private val preferences: EncryptedSharedPreferences
) : TokensStore {

    override fun storeUserToken(token: Token) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN_VALUE, token.accessToken)
            .putString(KEY_REFRESH_TOKEN_VALUE, token.refreshToken)
            .apply()
    }

    override fun clearUserToken() {
        preferences.edit()
            .remove(KEY_ACCESS_TOKEN_VALUE)
            .remove(KEY_REFRESH_TOKEN_VALUE)
            .apply()
    }

    override fun userIsSignedIn() = userToken != null

    override val userToken: Token?
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

    override fun storeClientToken(accessToken: String) {
        preferences.edit()
            .putString(KEY_CLIENT_TOKEN_VALUE, accessToken)
            .apply()
    }

    override val clientToken: String?
        get() = preferences.getString(KEY_CLIENT_TOKEN_VALUE, null)


    companion object {

        @JvmStatic
        private val FILE_NAME = BuildConfig.APPLICATION_ID + ".tokens_store"

        @JvmStatic
        private val KEY_ACCESS_TOKEN_VALUE = "access.value"

        @JvmStatic
        private val KEY_REFRESH_TOKEN_VALUE = "refresh.value"

        @JvmStatic
        private val KEY_CLIENT_TOKEN_VALUE = "client.value"

        @JvmStatic
        fun create(context: Context): SharedPrefsTokensStore {
            val encryptedSharedPrefs = EncryptedSharedPreferences.create(
                "tokens_store",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ) as EncryptedSharedPreferences
            return SharedPrefsTokensStore(encryptedSharedPrefs)
        }
    }
}
