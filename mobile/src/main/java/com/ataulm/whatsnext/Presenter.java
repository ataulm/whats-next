package com.ataulm.whatsnext;

import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class Presenter {

    private final WhatsNextService whatsNextService;

    private Disposable disposable;

    Presenter(WhatsNextService whatsNextService) {
        this.whatsNextService = whatsNextService;
    }

    void startPresenting() {
        whatsNextService.watchlistObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Film>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.d("!!!", "onSubscribe " + disposable);
                        Presenter.this.disposable = disposable;
                    }

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
