package com.ataulm.letterboxd

/**
 * Public API for this module. It implements [LetterboxdApi] because that class connects to the
 * Letterboxd.com service.
 */
interface LetterboxdRepository : LetterboxdApi {

    suspend fun login(username: String, password: String)

    fun logout()

    /**
     * Pseudo-check for isUserSignedIn. No guarantees the access token is valid, but can serve as a
     * proxy for whether the user thinks they're signed in (which can inform what UI to show).
     */
    fun hasUserAccessToken(): Boolean
}
