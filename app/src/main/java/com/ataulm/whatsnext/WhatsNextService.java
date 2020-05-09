package com.ataulm.whatsnext;

import com.ataulm.support.Clock;
import com.ataulm.whatsnext.api.ApiFilm;
import com.ataulm.whatsnext.api.ApiFilmRelationship;
import com.ataulm.whatsnext.api.ApiSearchResponse;
import com.ataulm.whatsnext.api.AuthTokenApiResponse;
import com.ataulm.whatsnext.api.FilmRelationshipConverter;
import com.ataulm.whatsnext.api.FilmSummaryConverter;
import com.ataulm.whatsnext.api.Letterboxd;
import com.ataulm.whatsnext.api.LetterboxdApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class WhatsNextService {

    @Deprecated
    private final Letterboxd letterboxd;
    private final LetterboxdApi letterboxdApi;
    private final FilmSummaryConverter filmSummaryConverter;
    private final FilmRelationshipConverter filmRelationshipConverter;
    private final TokensStore tokensStore;
    private final Clock clock;

    public WhatsNextService(Letterboxd letterboxd, LetterboxdApi letterboxdApi, FilmSummaryConverter filmSummaryConverter, FilmRelationshipConverter filmRelationshipConverter, TokensStore tokensStore, Clock clock) {
        this.letterboxd = letterboxd;
        this.letterboxdApi = letterboxdApi;
        this.filmSummaryConverter = filmSummaryConverter;
        this.filmRelationshipConverter = filmRelationshipConverter;
        this.tokensStore = tokensStore;
        this.clock = clock;
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
        ).toObservable();
    }
}
