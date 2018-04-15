package com.ataulm.whatsnext.letterboxd

import io.reactivex.Observable

internal class MockResponsesLetterboxdApi : LetterboxdApi {

    override fun accessToken(username: String, password: String, grantType: String): Observable<AccessToken> {
        TODO("not implemented")
    }

    override fun refreshAccessToken(refreshToken: String, grantType: String): Observable<AccessToken> {
        TODO("not implemented")
    }

    override fun search(input: String): Observable<SearchResponse> {
        TODO("not implemented")
    }

    override fun film(id: String): Observable<Film> {
        TODO("not implemented")
    }
}
