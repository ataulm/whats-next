package com.ataulm.whatsnext.film;

import android.support.annotation.Nullable;
import android.util.Log;

import com.ataulm.whatsnext.ErrorTrackingDisposableObserver;
import com.ataulm.whatsnext.Film;
import com.ataulm.support.Toaster;
import com.ataulm.whatsnext.WhatsNextService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class FilmPresenter {

    private final WhatsNextService service;
    private final FilmDisplayer displayer;
    private final String filmId;

    @Nullable
    private Disposable disposable;

    FilmPresenter(WhatsNextService service, FilmDisplayer displayer, String filmId) {
        this.service = service;
        this.displayer = displayer;
        this.filmId = filmId;
    }

    void startPresenting() {
        displayer.attach(new FilmDisplayer.Callback() {
            @Override
            public void onClickMarkAsWatched() {
                Toaster.Companion.display("on click mark as watched");
            }

            @Override
            public void onClickMarkAsNotWatched() {
                Toaster.Companion.display("on click mark as not watched");
            }
        });

        disposable = service.film(filmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new ErrorTrackingDisposableObserver<Film>() {
                            @Override
                            public void onNext(Film film) {
                                Log.d("!!!", "onNext " + film.toString());
                                displayer.display(film);
                            }

                            @Override
                            public void onComplete() {
                                Log.d("!!!", "onComplete");
                            }
                        }
                );
    }

    void stopPresenting() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
