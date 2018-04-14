package com.ataulm.whatsnext.letterboxd

import io.reactivex.Observable

internal class MockResponsesLetterboxdApi : LetterboxdApi {

    override fun accessToken(username: String, password: String, grantType: String): Observable<LetterboxdApi.AccessToken> {
        TODO("not implemented")
    }
}
