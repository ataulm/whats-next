package com.ataulm.whatsnext.api.auth

import androidx.annotation.WorkerThread
import com.ataulm.letterboxd.auth.LetterboxdAuthApi
import com.ataulm.whatsnext.AuthRepository
import com.ataulm.whatsnext.LocalTokensStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LetterboxdAuthRepository @Inject constructor(
    private val letterboxdAuthApi: LetterboxdAuthApi,
    private val localTokensStorage: LocalTokensStorage
) : AuthRepository {

    override suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        val authTokenApiResponse = letterboxdAuthApi.fetchUserTokens(username, password)
        localTokensStorage.storeUserAccessToken(authTokenApiResponse.accessToken)
        localTokensStorage.storeUserRefreshToken(authTokenApiResponse.refreshToken)
    }

    override fun getUserAccessToken(): String? {
        return localTokensStorage.userAccessToken()
    }

    override fun getClientAccessToken(): String? {
        return localTokensStorage.clientAccessToken()
    }

    @WorkerThread
    override fun refreshUserAccessToken(): String {
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
    override fun refreshClientAccessToken(): String {
        runCatching {
            letterboxdAuthApi.fetchClientAccessToken().execute().body()
        }.getOrNull()?.let { apiResponse ->
            return apiResponse.accessToken
                .also { localTokensStorage.storeClientAccessToken(it) }
        } ?: throw AuthError.FetchingClientToken
    }
}
