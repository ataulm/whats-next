package com.ataulm.whatsnext.api.auth

import androidx.annotation.WorkerThread
import com.ataulm.letterboxd.auth.LetterboxdAuthApi
import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.TokensStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val letterboxdAuthApi: LetterboxdAuthApi,
    private val tokensStore: TokensStore
) {

    suspend fun login(username: String, password: String) = withContext(Dispatchers.IO) {
        val authTokenApiResponse = letterboxdAuthApi.fetchUserAuthTokens(username, password)
        Token(authTokenApiResponse.accessToken, authTokenApiResponse.refreshToken)
            .also { tokensStore.storeUserToken(it) }
    }

    fun getUserAccessToken(): String? {
        return tokensStore.userToken?.accessToken
    }

    fun getClientAccessToken(): String? {
        return tokensStore.clientToken
    }

    @WorkerThread
    fun refreshUserToken(): Token {
        val userToken = tokensStore.userToken ?: throw AuthError.MissingUserToken
        runCatching {
            letterboxdAuthApi.refreshAuthTokens(userToken.refreshToken).execute().body()
        }.getOrNull()?.let { apiResponse ->
            return Token(apiResponse.accessToken, apiResponse.refreshToken)
                .also { tokensStore.storeUserToken(it) }
        } ?: throw AuthError.RefreshingUserToken
    }

    @WorkerThread
    fun refreshClientToken(): String {
        runCatching {
            letterboxdAuthApi.fetchAnonymousAuthToken().execute().body()
        }.getOrNull()?.let { apiResponse ->
            return apiResponse.accessToken
                .also { tokensStore.storeClientToken(it) }
        } ?: throw AuthError.FetchingClientToken
    }
}
