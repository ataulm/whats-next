package com.ataulm.whatsnext.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.ErrorTrackingDisposableObserver
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.WhatsNextService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class SearchViewModel(private val service: WhatsNextService) : ViewModel() {

    private val _films = MutableLiveData<List<FilmSummary>>()
    val films: LiveData<List<FilmSummary>> = _films

    private var disposable: Disposable? = null

    fun onSearch(searchTerm: String) {
        disposable?.dispose()
        disposable = service.search(searchTerm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        object : ErrorTrackingDisposableObserver<List<FilmSummary>>() {
                            override fun onNext(filmSummaries: List<FilmSummary>) {
                                Log.d("!!!", "onNext $filmSummaries")
                                _films.value = filmSummaries
                            }

                            override fun onComplete() {
                                Log.d("!!!", "onComplete")
                            }
                        }
                )
    }

    fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}

internal class SearchViewModelFactory(private val service: WhatsNextService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = SearchViewModel(service) as T
}
