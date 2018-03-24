package com.ataulm.whatsnext.film

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.ataulm.whatsnext.ErrorTrackingDisposableObserver
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.WhatsNextService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FilmLiveData(val service: WhatsNextService, val filmId: String) : MutableLiveData<Film>() {

    private var disposable: Disposable? = null

    override fun onActive() {
        disposable = service.film(filmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        object : ErrorTrackingDisposableObserver<Film>() {
                            override fun onNext(film: Film) {
                                Log.d("!!!", "onNext " + film.toString())
                                value = film
                            }
                        }
                )
    }

    override fun onInactive() {
        disposable?.dispose()
    }
}
