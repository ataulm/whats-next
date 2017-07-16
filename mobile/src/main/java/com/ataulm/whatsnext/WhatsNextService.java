package com.ataulm.whatsnext;

import com.ataulm.whatsnext.letterboxd.Api;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class WhatsNextService {

    private final Api api;
    private final TokensStore tokensStore;
    private final Clock clock;

    WhatsNextService(Api api, TokensStore tokensStore, Clock clock) {
        this.api = api;
        this.tokensStore = tokensStore;
        this.clock = clock;
    }

    public Observable<List<FilmSummary>> search(final String searchTerm) {
        return Observable.fromCallable(new Callable<List<FilmSummary>>() {
            @Override
            public List<FilmSummary> call() throws Exception {
                return api.search(searchTerm);
            }
        });
    }

    public Observable<Film> film(final String letterboxdId) {
        return Observable.fromCallable(new Callable<Film>() {
            @Override
            public Film call() throws Exception {
                return api.film(letterboxdId, getToken().getAccessToken());
            }
        });
    }

    private Token getToken() throws IOException {
        Token token = tokensStore.getToken();
        if (token == null) {
            token = api.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
            tokensStore.store(token);
        } else if (token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis()) {
            token = api.refreshAccessToken(token.getRefreshToken());
            tokensStore.store(token);
        }
        return token;
    }
}
