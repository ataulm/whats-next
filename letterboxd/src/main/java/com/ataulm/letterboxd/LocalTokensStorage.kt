package com.ataulm.letterboxd

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class LocalTokensStorage private constructor(
    private val preferences: EncryptedSharedPreferences
) {

    fun clientAccessToken(): String? {
        return preferences.getString(KEY_CLIENT_TOKEN_VALUE, null)
    }

    fun userAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN_VALUE, null)
    }

    fun userRefreshToken(): String? {
        return preferences.getString(KEY_REFRESH_TOKEN_VALUE, null)
    }

    fun storeClientAccessToken(token: String) {
        preferences.edit()
            .putString(KEY_CLIENT_TOKEN_VALUE, token)
            .apply()
    }

    fun storeUserAccessToken(token: String) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN_VALUE, token)
            .apply()
    }

    fun storeUserRefreshToken(token: String) {
        preferences.edit()
            .putString(KEY_REFRESH_TOKEN_VALUE, token)
            .apply()
    }

    companion object {

        @JvmStatic
        private val FILE_NAME = BuildConfig.LIBRARY_PACKAGE_NAME + ".tokens_store"

        @JvmStatic
        private val KEY_ACCESS_TOKEN_VALUE = "access.value"

        @JvmStatic
        private val KEY_REFRESH_TOKEN_VALUE = "refresh.value"

        @JvmStatic
        private val KEY_CLIENT_TOKEN_VALUE = "client.value"

        @JvmStatic
        fun create(context: Context): LocalTokensStorage {
            val encryptedSharedPrefs = EncryptedSharedPreferences.create(
                FILE_NAME,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ) as EncryptedSharedPreferences
            return LocalTokensStorage(encryptedSharedPrefs)
        }
    }
}
