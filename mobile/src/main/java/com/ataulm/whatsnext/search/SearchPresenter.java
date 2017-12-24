package com.ataulm.whatsnext.search;

import android.support.annotation.Nullable;
import android.util.Log;

import com.ataulm.support.Clock;
import com.ataulm.whatsnext.ErrorTrackingDisposableObserver;
import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.Navigator;
import com.ataulm.whatsnext.Token;
import com.ataulm.whatsnext.TokensStore;
import com.ataulm.whatsnext.WhatsNextService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class SearchPresenter {

    private final WhatsNextService service;
    private final SearchDisplayer displayer;
    private final TokensStore tokensStore;
    private final Clock clock;
    private final Navigator navigator;

    @Nullable
    private Disposable disposable;

    SearchPresenter(WhatsNextService service, SearchDisplayer displayer, TokensStore tokensStore, Clock clock, Navigator navigator) {
        this.service = service;
        this.displayer = displayer;
        this.tokensStore = tokensStore;
        this.clock = clock;
        this.navigator = navigator;
    }

    void startPresenting() {
        displayer.attach(new SearchDisplayer.Callback() {
            @Override
            public void onSearch(String searchTerm) {
                disposable = service.search(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new ErrorTrackingDisposableObserver<List<FilmSummary>>() {
                                    @Override
                                    public void onNext(List<FilmSummary> filmSummaries) {
                                        Log.d("!!!", "onNext " + filmSummaries.toString());
                                        displayer.display(filmSummaries);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d("!!!", "onComplete");
                                    }
                                }
                        );
            }

            @Override
            public void onClick(FilmSummary filmSummary) {
                navigator.navigateToFilm(filmSummary.getIds().getLetterboxd());
            }

            @Override
            public void onClickSignIn() {
                Token token = tokensStore.getToken();
                if (token == null || expired(token)) {
                    navigator.navigateToSignIn();
                } else {
                    displayer.toastAlreadySignedIn();
                }
            }

            private boolean expired(Token token) {
                return token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis();
            }
        });
    }

    void stopPresenting() {
        if (disposable != null) {
            disposable.dispose();
        }
        displayer.detachCallback();
    }
}
