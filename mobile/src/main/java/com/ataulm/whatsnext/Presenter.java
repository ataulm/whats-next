package com.ataulm.whatsnext;

import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class Presenter {

    private final WhatsNextService whatsNextService;
    private final Displayer displayer;

    private Disposable disposable;

    Presenter(WhatsNextService whatsNextService, Displayer displayer) {
        this.whatsNextService = whatsNextService;
        this.displayer = displayer;
    }

    void startPresenting() {
        disposable = whatsNextService.watchlistObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Film>>() {
                    @Override
                    public void onNext(List<Film> films) {
                        Log.d("!!!", "onNext " + films.toString());
                        displayer.display(films);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("!!!", "onError", e);
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
