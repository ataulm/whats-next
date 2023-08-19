package com.ataulm.whatsnext.api

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

private const val API_KEY = "test_api_key_do_not_change"
private const val API_SECRET = "test_api_secret_do_not_change"
private const val REQUEST_URL =
    "https://api.letterboxd.com/api/v0/films?apikey=$API_KEY&nonce=4be61b1d-e192-457f-8d13-a1bf812d8070&timestamp=1692472097&perPage=5"

/**
 * The Letterboxd API wants requests to include a signature that's calculated in a very specific way.
 * I used the [example Ruby client](https://github.com/grantyb/letterboxd-api-example-ruby-client)
 * to determine the expected signatures for a given set of inputs.
 */
class GenerateSignatureTest {

    @Test
    fun `generates same signature as ruby client for GET method`() {
        val signature = generateSignature(
            body = null,
            method = "GET",
            url = REQUEST_URL.toHttpUrl(),
            apiSecret = API_SECRET
        )

        val expectedSignatureFromRubyClientExample =
            "ed37678b209de1abafe6d966b421c8b92d325645a12b16e2cb5535fe5b92f503"

        assertThat(signature)
            .isEqualTo(expectedSignatureFromRubyClientExample)
    }
}
