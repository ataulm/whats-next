package com.ataulm.whatsnext;

import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class Presenter {

    private final WhatsNextService whatsNextService;
    private final FilmsDisplayer filmsDisplayer;

    private Disposable disposable;

    Presenter(WhatsNextService whatsNextService, FilmsDisplayer filmsDisplayer) {
        this.whatsNextService = whatsNextService;
        this.filmsDisplayer = filmsDisplayer;
    }

    void startPresenting() {
        disposable = whatsNextService.watchlistObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Film>>() {
                    @Override
                    public void onNext(List<Film> films) {
                        Log.d("!!!", "onNext " + films.toString());
                        filmsDisplayer.display(films);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("!!!", "onError", e);
                        filmsDisplayer.displayError("error: " + e.getMessage());
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
