package com.ataulm.whatsnext.api

import com.ataulm.whatsnext.TokensStore
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_AUTH = "Authorization"

// TODO: The docs and Ruby client (and this client) all have different implementations
//  re: when/where to add the signature. Revisit this, using the Ruby client to test,
//  then ping Letterboxd to update the API when it's verified
//  Upgrade GenerateSignatureTest into a test for this class.
internal class AddAuthorizationHeaderInterceptor(
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

        val amendedRequest = chain.request().newBuilder()

        if (chain.request().requiresAuthenticatedUser()) {
            amendedRequest.addHeader(HEADER_AUTH, "Bearer ${requireNotNull(tokensStore.token).accessToken}")
            val url = chain.request().url.newBuilder()
                .addQueryParameter("signature", signature)
                .build()
            amendedRequest.url(url)
        } else {
            amendedRequest.addHeader(HEADER_AUTH, "Signature $signature")
        }



//        if (chain.request().requiresAuthenticatedUser()) {
//            amendedRequest.addHeader(
//                HEADER_AUTH,
//                "Bearer ${requireNotNull(tokensStore.token).accessToken}"
//            )
//        }
//
//        if (chain.request().isFormUrlEncoded()) {
//            // this isn't in the docs but is here (and is necessary) https://github.com/grantyb/letterboxd-api-example-ruby-client
//            amendedRequest.addHeader(HEADER_AUTH, "Signature $signature")
//        } else {
//            // https://api-docs.letterboxd.com/#signing
//            amendedRequest.url(
//                chain.request().url.newBuilder()
//                    .addQueryParameter("signature", signature)
//                    .build()
//            )
//        }

        return chain.proceed(amendedRequest.build())
    }
}
