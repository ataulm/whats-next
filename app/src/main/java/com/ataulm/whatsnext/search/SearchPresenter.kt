package com.ataulm.whatsnext.search

import android.util.Log
import com.ataulm.whatsnext.ErrorTrackingDisposableObserver
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.Navigator
import com.ataulm.whatsnext.WhatsNextService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class SearchPresenter(
        private val service: WhatsNextService,
        private val navigator: Navigator
) {

    private var disposable: Disposable? = null

    fun onSearch(searchTerm: String, onNext: ((List<FilmSummary>) -> Unit)) {
        disposable?.dispose()
        disposable = service.search(searchTerm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        object : ErrorTrackingDisposableObserver<List<FilmSummary>>() {
                            override fun onNext(filmSummaries: List<FilmSummary>) {
                                Log.d("!!!", "onNext $filmSummaries")
                                onNext(filmSummaries)
                            }

                            override fun onComplete() {
                                Log.d("!!!", "onComplete")
                            }
                        }
                )
    }

    fun onClick(filmSummary: FilmSummary) {
        navigator.navigateToFilm(filmSummary.ids.letterboxd)
    }

    fun stopPresenting() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
