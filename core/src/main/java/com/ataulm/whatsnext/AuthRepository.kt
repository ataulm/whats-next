package com.ataulm.whatsnext

interface AuthRepository {

    /**
     * Exchange credentials for user tokens and store them
     */
    suspend fun login(username: String, password: String)

    /**
     * Returns the cached user access token
     */
    fun getUserAccessToken(): String?

    /**
     * Synchronously refresh the user tokens using the cached refresh token
     * @return the new user access token
     */
    fun refreshUserAccessToken(): String

    /**
     * Returns the cached user access token
     */
    fun getClientAccessToken(): String?

    /**
     * Synchronously refresh the client access token
     * @return the new client access token
     */
    fun refreshClientAccessToken(): String
}
