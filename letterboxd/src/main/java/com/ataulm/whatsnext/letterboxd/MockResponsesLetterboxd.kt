package com.ataulm.whatsnext.letterboxd

import io.reactivex.Observable

internal class MockResponsesLetterboxd : Letterboxd {

    override fun accessToken(username: String, password: String, grantType: String): Observable<Letterboxd.AccessToken> {
        TODO("not implemented")
    }
}
