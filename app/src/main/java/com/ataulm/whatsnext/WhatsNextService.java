package com.ataulm.whatsnext;

import com.ataulm.whatsnext.api.ApiFilm;
import com.ataulm.whatsnext.api.ApiFilmRelationship;
import com.ataulm.whatsnext.api.ApiSearchResponse;
import com.ataulm.whatsnext.api.AuthTokenApiResponse;
import com.ataulm.whatsnext.api.FilmRelationshipConverter;
import com.ataulm.whatsnext.api.FilmSummaryConverter;
import com.ataulm.whatsnext.api.LetterboxdApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

public class WhatsNextService {

    private final LetterboxdApi letterboxdApi;
    private final TokensStore tokensStore;
    private final FilmSummaryConverter filmSummaryConverter;
    private final FilmRelationshipConverter filmRelationshipConverter;

    public WhatsNextService(LetterboxdApi letterboxdApi, TokensStore tokensStore, FilmSummaryConverter filmSummaryConverter, FilmRelationshipConverter filmRelationshipConverter) {
        this.letterboxdApi = letterboxdApi;
        this.tokensStore = tokensStore;
        this.filmSummaryConverter = filmSummaryConverter;
        this.filmRelationshipConverter = filmRelationshipConverter;
    }

    public Observable<Token> login(final String username, final String password) {
        return letterboxdApi.fetchAuthToken(username, password, "password")
                .toObservable()
                .map(toToken());
    }

    private Function<AuthTokenApiResponse, Token> toToken() {
        return new Function<AuthTokenApiResponse, Token>() {
            @Override
            public Token apply(AuthTokenApiResponse authTokenApiResponse) {
                return new Token(
                        authTokenApiResponse.getAccessToken(),
                        authTokenApiResponse.getRefreshToken(),
                        authTokenApiResponse.getSecondsUntilExpiry()
                );
            }
        };
    }

    public Observable<List<FilmSummary>> search(final String searchTerm) {
        return letterboxdApi.search(searchTerm)
                .toObservable()
                .map(new Function<ApiSearchResponse, List<FilmSummary>>() {
                    @Override
                    public List<FilmSummary> apply(ApiSearchResponse apiSearchResponse) {
                        List<FilmSummary> filmSummaries = new ArrayList<>(apiSearchResponse.searchItems.size());
                        for (ApiSearchResponse.Result searchItem : apiSearchResponse.searchItems) {
                            if (!"FilmSearchItem".equals(searchItem.type)) {
                                continue;
                            }
                            FilmSummary filmSummary = filmSummaryConverter.convert(searchItem.filmSummary);
                            filmSummaries.add(filmSummary);
                        }
                        return filmSummaries;
                    }
                });
    }

    public Observable<Film> film(final String letterboxdId) {
        return Single.zip(
                letterboxdApi.film(letterboxdId),
                letterboxdApi.filmRelationship(letterboxdId),
                new BiFunction<ApiFilm, ApiFilmRelationship, Film>() {
                    @Override
                    public Film apply(ApiFilm apiFilm, ApiFilmRelationship apiFilmRelationship) {
                        return new Film(
                                filmSummaryConverter.convert(apiFilm),
                                filmRelationshipConverter.convert(apiFilmRelationship)
                        );
                    }
                }
        )
                .toObservable()
                .compose(this.<Film>refreshAccessTokenIfNecessary());
    }

    private <T> ObservableTransformer<T, T> refreshAccessTokenIfNecessary() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(final Observable<T> upstream) {
                return upstream.onErrorResumeNext(new Function<Throwable, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Throwable throwable) {
                        if (throwable instanceof HttpException && ((HttpException) throwable).code() == 401) {
                            return refreshAccessToken().andThen(upstream);
                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        };
    }

    private Completable refreshAccessToken() {
        Token token = tokensStore.getToken();
        if (token == null) {
            return Completable.error(new UserRequiresSignInException());
        }
        String refreshToken = tokensStore.getToken().getRefreshToken();
        return letterboxdApi.refreshAuthToken(refreshToken, "refresh_token")
                .map(toToken())
                .flatMapCompletable(new Function<Token, CompletableSource>() {
                    @Override
                    public CompletableSource apply(final Token token) {
                        return Completable.fromAction(new Action() {
                            @Override
                            public void run() {
                                tokensStore.store(token);
                            }
                        });
                    }
                });
    }
}

class UserRequiresSignInException extends RuntimeException {
}