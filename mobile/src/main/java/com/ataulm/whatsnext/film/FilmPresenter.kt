package com.ataulm.whatsnext.film

import android.util.Log
import com.ataulm.support.Toaster
import com.ataulm.whatsnext.ErrorTrackingDisposableObserver
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.WhatsNextService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class FilmPresenter(private val service: WhatsNextService, private val displayer: FilmDisplayer, private val filmId: String) {

    private lateinit var disposable: Disposable

    fun startPresenting() {
        displayer.attach(object : FilmDisplayer.Callback {
            override fun onClickMarkAsWatched() {
                Toaster.display("on click mark as watched")
            }

            override fun onClickMarkAsNotWatched() {
                Toaster.display("on click mark as not watched")
            }
        })

        disposable = service.film(filmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        object : ErrorTrackingDisposableObserver<Film>() {
                            override fun onNext(film: Film) {
                                Log.d("!!!", "onNext " + film.toString())
                                displayer.display(film)
                            }
                        }
                )
    }

    fun stopPresenting() {
        disposable.dispose()
        displayer.detachCallback()
    }
}
