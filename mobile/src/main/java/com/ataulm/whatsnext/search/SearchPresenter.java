package com.ataulm.whatsnext.search;

import android.support.annotation.Nullable;
import android.util.Log;

import com.ataulm.whatsnext.ErrorTrackingDisposableObserver;
import com.ataulm.whatsnext.Film;
import com.ataulm.whatsnext.WhatsNextService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class SearchPresenter {

    private final WhatsNextService service;
    private final SearchDisplayer displayer;

    @Nullable
    private Disposable disposable;

    SearchPresenter(WhatsNextService service, SearchDisplayer displayer) {
        this.service = service;
        this.displayer = displayer;
    }

    void startPresenting() {
        displayer.attach(new SearchDisplayer.Callback() {
            @Override
            public void onSearch(String searchTerm) {
                disposable = service.search(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new ErrorTrackingDisposableObserver<List<Film>>() {
                                    @Override
                                    public void onNext(List<Film> films) {
                                        Log.d("!!!", "onNext " + films.toString());
                                        displayer.display(films);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d("!!!", "onComplete");
                                    }
                                }
                        );
            }
        });
    }

    void stopPresenting() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
