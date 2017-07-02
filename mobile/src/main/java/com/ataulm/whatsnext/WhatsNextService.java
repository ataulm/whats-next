package com.ataulm.whatsnext;

import com.ataulm.whatsnext.letterboxd.LetterboxdApi;

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

    public Observable<List<Film>> watchlistObservable() {
        return Observable.fromCallable(new Callable<List<Film>>() {
            @Override
            public List<Film> call() throws Exception {
                Token token = tokensStore.getToken();
                if (token == null || token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis()) {
                    token = letterboxdApi.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
                    tokensStore.store(token);
                }
                String letterboxId = letterboxdApi.me(token.getAccessToken()).member.letterboxId;
                return letterboxdApi.watchlist(token.getAccessToken(), letterboxId);
            }
        });
    }
}
