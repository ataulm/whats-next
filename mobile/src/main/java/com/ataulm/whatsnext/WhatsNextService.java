package com.ataulm.whatsnext;

import com.ataulm.support.Clock;
import com.ataulm.whatsnext.api.Letterboxd;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class WhatsNextService {

    private final Letterboxd letterboxd;
    private final TokensStore tokensStore;
    private final Clock clock;

    WhatsNextService(Letterboxd letterboxd, TokensStore tokensStore, Clock clock) {
        this.letterboxd = letterboxd;
        this.tokensStore = tokensStore;
        this.clock = clock;
    }

    public Observable<Token> login(final String username, final String password) {
        return Observable.fromCallable(new Callable<Token>() {
            @Override
            public Token call() throws Exception {
                return letterboxd.fetchAccessToken(username, password);
            }
        });
    }

    public Observable<List<FilmSummary>> search(final String searchTerm) {
        return Observable.fromCallable(new Callable<List<FilmSummary>>() {
            @Override
            public List<FilmSummary> call() throws Exception {
                return letterboxd.search(searchTerm);
            }
        });
    }

    public Observable<Film> film(final String letterboxdId) {
        return Observable.fromCallable(new Callable<Film>() {
            @Override
            public Film call() throws Exception {
                return letterboxd.film(letterboxdId, getToken().getAccessToken());
            }
        });
    }

    private Token getToken() throws IOException {
        Token token = tokensStore.getToken();
        if (token == null) {
            token = letterboxd.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
            tokensStore.store(token);
        } else if (token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis()) {
            // TODO: if refreshing the TokenFails, this error needs to bubble up so we can get user to sign in again
            token = letterboxd.refreshAccessToken(token.getRefreshToken());
            tokensStore.store(token);
        }
        return token;
    }
}
