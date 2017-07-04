package com.ataulm.whatsnext;

import com.ataulm.whatsnext.letterboxd.LetterboxdApi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

class WhatsNextService {

    private final LetterboxdApi letterboxdApi;
    private final TokensStore tokensStore;
    private final Clock clock;

    WhatsNextService(LetterboxdApi letterboxdApi, TokensStore tokensStore, Clock clock) {
        this.letterboxdApi = letterboxdApi;
        this.tokensStore = tokensStore;
        this.clock = clock;
    }

    Observable<List<Film>> watchlistObservable() {
        return Observable.fromCallable(new Callable<List<Film>>() {
            @Override
            public List<Film> call() throws Exception {
                Token token = getToken();
                String letterboxId = letterboxdApi.me(token.getAccessToken()).member.letterboxId;
                return letterboxdApi.watchlist(token.getAccessToken(), letterboxId);
            }
        });
    }

    private Token getToken() throws IOException {
        Token token = tokensStore.getToken();
        if (token == null) {
            token = letterboxdApi.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
            tokensStore.store(token);
        } else if (token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis()) {
            token = letterboxdApi.refreshAccessToken(token.getRefreshToken());
            tokensStore.store(token);
        }
        return token;
    }
}
