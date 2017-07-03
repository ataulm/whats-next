package com.ataulm.whatsnext;

import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class Presenter {

    private final WhatsNextService whatsNextService;

    private Disposable disposable;

    Presenter(WhatsNextService whatsNextService) {
        this.whatsNextService = whatsNextService;
    }

    void startPresenting() {
        disposable = whatsNextService.watchlistObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Film>>() {
                    @Override
                    public void onNext(List<Film> films) {
                        Log.d("!!!", "onNext " + films.toString());
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
