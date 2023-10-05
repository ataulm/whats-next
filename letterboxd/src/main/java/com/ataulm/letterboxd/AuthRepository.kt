package com.ataulm.letterboxd

import androidx.annotation.WorkerThread
import com.ataulm.letterboxd.auth.AuthError
import com.ataulm.letterboxd.auth.LetterboxdAuthApi
import com.ataulm.whatsnext.core.IoContext
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
internal class AuthRepository @Inject constructor(
    private val letterboxdAuthApi: LetterboxdAuthApi,
    private val localTokensStorage: LocalTokensStorage,
    @IoContext private val ioContext: CoroutineContext
) {

    suspend fun login(username: String, password: String) = withContext(ioContext) {
        val authTokenApiResponse = letterboxdAuthApi.fetchUserTokens(username, password)
        localTokensStorage.storeUserAccessToken(authTokenApiResponse.accessToken)
        localTokensStorage.storeUserRefreshToken(authTokenApiResponse.refreshToken)
    }

    fun clearUserTokens() {
        localTokensStorage.clearUserTokens()
    }

    fun getUserAccessToken(): String? {
        return localTokensStorage.userAccessToken()
    }

    fun getClientAccessToken(): String? {
        return localTokensStorage.clientAccessToken()
    }

    @WorkerThread
    fun refreshUserAccessToken(): String {
        val refreshToken = localTokensStorage.userRefreshToken() ?: throw AuthError.MissingUserToken
        runCatching {
            letterboxdAuthApi.refreshUserTokens(refreshToken).execute().body()
        }.getOrNull()?.let { apiResponse ->
            localTokensStorage.storeUserAccessToken(apiResponse.accessToken)
            localTokensStorage.storeUserRefreshToken(apiResponse.refreshToken)
            return apiResponse.accessToken
        } ?: throw AuthError.RefreshingUserToken
    }

    @WorkerThread
    fun refreshClientAccessToken(): String {
        runCatching {
            letterboxdAuthApi.fetchClientAccessToken().execute().body()
        }.getOrNull()?.let { apiResponse ->
            return apiResponse.accessToken
                .also { localTokensStorage.storeClientAccessToken(it) }
        } ?: throw AuthError.FetchingClientToken
    }
}
