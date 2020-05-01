package com.ataulm.whatsnext;

import com.ataulm.support.Clock;
import com.ataulm.whatsnext.api.AuthTokenApiResponse;
import com.ataulm.whatsnext.api.AuthenticationError;
import com.ataulm.whatsnext.api.AuthenticationError.Type;
import com.ataulm.whatsnext.api.Letterboxd;
import com.ataulm.whatsnext.api.LetterboxdApi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class WhatsNextService {

    private final Letterboxd letterboxd;
    private final LetterboxdApi letterboxdApi;
    private final TokensStore tokensStore;
    private final Clock clock;

    public WhatsNextService(Letterboxd letterboxd, LetterboxdApi letterboxdApi, TokensStore tokensStore, Clock clock) {
        this.letterboxd = letterboxd;
        this.letterboxdApi = letterboxdApi;
        this.tokensStore = tokensStore;
        this.clock = clock;
    }

    public Observable<Token> login(final String username, final String password) {
        return letterboxdApi.fetchAuthToken(username, password, "password")
                .toObservable()
                .map(new Function<AuthTokenApiResponse, Token>() {
                    @Override
                    public Token apply(AuthTokenApiResponse authTokenApiResponse) {
                        return new Token(
                                authTokenApiResponse.getAccessToken(),
                                authTokenApiResponse.getRefreshToken(),
                                authTokenApiResponse.getSecondsUntilExpiry()
                        );
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

    private Token getToken() {
        Token token = tokensStore.getToken();

        if (token == null) {
            throw new AuthenticationError(Type.REQUIRES_USER_SIGN_IN);
        }

        if (token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis()) {
            token = refreshToken(token);
            tokensStore.store(token);
        }

        return token;
    }

    private Token refreshToken(Token token) {
        try {
            return letterboxd.refreshAccessToken(token.getRefreshToken());
        } catch (IOException e) {
            throw new AuthenticationError(e, Type.EXCHANGING_REFRESH_TOKEN_FOR_FRESH_TOKEN);
        }
    }
}
