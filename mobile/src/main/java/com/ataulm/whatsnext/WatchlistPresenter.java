package com.ataulm.whatsnext;

import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class WatchlistPresenter {

    private final WhatsNextService whatsNextService;
    private final WatchlistDisplayer watchlistDisplayer;

    private Disposable disposable;

    WatchlistPresenter(WhatsNextService whatsNextService, WatchlistDisplayer watchlistDisplayer) {
        this.whatsNextService = whatsNextService;
        this.watchlistDisplayer = watchlistDisplayer;
    }

    void startPresenting() {
        disposable = whatsNextService.watchlist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorTrackingDisposableObserver<List<Film>>() {
                    @Override
                    public void onNext(List<Film> films) {
                        Log.d("!!!", "onNext " + films.toString());
                        watchlistDisplayer.display(films);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        watchlistDisplayer.displayError("error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("!!!", "onComplete");
                    }
                });
    }

    void stopPresenting() {
        disposable.dispose();
    }
}
