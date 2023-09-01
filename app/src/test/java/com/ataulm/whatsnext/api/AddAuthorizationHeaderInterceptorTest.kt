package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.TokensStore
import org.junit.Test

private const val API_SECRET = "secret"
class AddAuthorizationHeaderInterceptorTest {

    private val tokensStore = InMemoryTokensStore()
    private val interceptor = AddAuthorizationHeaderInterceptor(
        apiSecret = API_SECRET,
        tokensStore = tokensStore
    )

    @Test
    fun name() {

    }


}
