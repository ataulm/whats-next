package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.TokensStore
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.util.Locale

private const val HEADER_AUTH = "Authorization"

// TODO: The docs and Ruby client (and this client) all have different implementations
//  re: when/where to add the signature. Revisit this, using the Ruby client to test,
//  then ping Letterboxd to update the API when it's verified
//  Upgrade GenerateSignatureTest into a test for this class.
internal class AddAuthorizationInterceptor(
    private val apiSecret: String,
    private val tokensStore: TokensStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val signature = generateSignature(
            request.body,
            request.method,
            request.url,
            apiSecret
        )

        val amendedRequest = request.newBuilder()

        tokensStore.token?.accessToken?.let { accessToken ->
            amendedRequest.addHeader(HEADER_AUTH, "Bearer $accessToken")
        }

        if (request.isFormUrlEncoded()) {
            amendedRequest.addHeader(HEADER_AUTH, "Signature $signature")
        } else {
            val url = request.url.newBuilder()
                .addQueryParameter("signature", signature)
                .build()
            amendedRequest.url(url)
        }

        return chain.proceed(amendedRequest.build())
    }
}

// TODO: make this private after moving the tests
fun generateSignature(body: RequestBody?, method: String, url: HttpUrl, apiSecret: String): String {
    // TODO: check this works patch reqs with body
    val urlEncodedBody = body?.toUrlEncodedString() ?: ""
    val preHashedSignature = "${method.uppercase(Locale.US)}\u0000$url\u0000$urlEncodedBody"
    return HmacSha256.generateHash(apiSecret, preHashedSignature).lowercase(Locale.US)
}

private fun RequestBody.toUrlEncodedString() = Buffer().apply { writeTo(this) }.readUtf8()
