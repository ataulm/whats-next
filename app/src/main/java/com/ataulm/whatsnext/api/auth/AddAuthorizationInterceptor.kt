package com.ataulm.whatsnext.api.auth

import com.ataulm.whatsnext.api.RequiresAuthenticatedUser
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Invocation
import timber.log.Timber
import javax.inject.Inject

private const val HEADER_AUTH = "Authorization"
private const val HEADER_AUTH_PREFIX = "Bearer"

/**
 * Sept 2023, The latest API authorizes each request with a Bearer access token.
 * Either an authenticated (user) access token or an unauthenticated (client) token.
 *
 * Don't apply this to [LetterboxdAuthApi] because those don't require `Authorization` header.
 */
class AddAuthorizationInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Chain) = if (chain.request().requiresAuthenticatedUser()) {
        addUserTokenToRequest(chain)
    } else {
        addClientTokenToRequest(chain)
    }

    private fun addUserTokenToRequest(chain: Chain): Response {
        // can't refresh/recover if we don't even have the token(s) to begin with
        val accessToken = authRepository.getUserAccessToken() ?:  throw AuthError.MissingUserToken
        val requestWithAuth = chain.request().newBuilder()
            .header(HEADER_AUTH, "$HEADER_AUTH_PREFIX $accessToken")
            .build()
        return chain.proceed(requestWithAuth)
    }

    private fun addClientTokenToRequest(chain: Chain): Response {
        // this might be null but we'll let the authenticator handle the recovery
        val accessToken = authRepository.getClientAccessToken()
        val requestWithAuth = chain.request().newBuilder()
            .header(HEADER_AUTH, "$HEADER_AUTH_PREFIX $accessToken")
            .build()
        return chain.proceed(requestWithAuth)
    }
}

class LetterboxdAuthenticator @Inject constructor(
    private val authRepository: AuthRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val request = response.request
        // we've got new token & retried, but still get 401, give up
        if (response.responseCount > 5) return null

        val requestToken = request.header(HEADER_AUTH)?.replace("$HEADER_AUTH_PREFIX ", "")

        val updatedToken: String? = if (request.requiresAuthenticatedUser()) {
            // synchronizing to prevent multiple `getNewToken` calls when many parallel requests have got 401
            // see https://stackoverflow.com/questions/22450036/refreshing-oauth-token-using-retrofit-without-modifying-all-calls#comment72191030_31624433
            synchronized(this) {
                if (requestToken != authRepository.getUserAccessToken()) {  // token is already updated by another request
                    authRepository.getUserAccessToken()
                } else {
                    authRepository.refreshUserToken().accessToken
                }
            }
        } else {
            // synchronizing to prevent multiple `getNewToken` calls when many parallel requests have got 401
            // see https://stackoverflow.com/questions/22450036/refreshing-oauth-token-using-retrofit-without-modifying-all-calls#comment72191030_31624433
            synchronized(this) {
                if (requestToken != authRepository.getClientAccessToken()) {  // token is already updated by another request
                    authRepository.getClientAccessToken()
                } else {
                    authRepository.refreshClientToken()
                }
            }
        }
        return request.newBuilder()
            .header(HEADER_AUTH, "$HEADER_AUTH_PREFIX $updatedToken")
            .build()
    }
}

private val Response.responseCount: Int
    get() = generateSequence(this) { it.priorResponse }.count()

private fun Request.requiresAuthenticatedUser(): Boolean {
    return tag(Invocation::class.java)
        ?.method()
        ?.getAnnotation(RequiresAuthenticatedUser::class.java) != null
}
