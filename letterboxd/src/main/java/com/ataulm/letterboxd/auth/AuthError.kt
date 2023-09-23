package com.ataulm.letterboxd.auth

import java.io.IOException

/**
 * These have to be [IOException] because otherwise OkHttp won't propagate them.
 */
sealed class AuthError : IOException() {

    /**
     * Problem signing in - probably incorrect user/pass?
     */
    data object SigningIn : AuthError()

    /**
     * Problem while using the refresh token - probably expired so the user needs to sign in again.
     */
    data object RefreshingUserToken : AuthError()

    /**
     * Likely to be a developer error: accessing a user resource without checking that the user
     * token is present (and direct the user to sign in).
     * Maybe a user issue, if they cleared app storage (the user token) and resumed the app on a
     * screen that has a user affordance like rating (though not sure whether the app is killed in
     * this case).
     *
     * In any case, should direct the user to sign in again (and keep an eye on these errors if they
     * occur frequently because that suggests it's a developer error).
     */
    data object MissingUserToken : AuthError()

    /**
     * Problem while trying to fetch a new client token. Since this doesn't require user input, it's
     * not really recoverable. Try again later because it might be a server issue?
     */
    data object FetchingClientToken : AuthError()
}