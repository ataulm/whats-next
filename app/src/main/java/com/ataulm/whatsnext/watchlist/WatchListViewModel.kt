package com.ataulm.whatsnext.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ataulm.support.Event
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.WhatsNextRepository
import kotlinx.coroutines.flow.Flow

internal class WatchListViewModel(private val repository: WhatsNextRepository) : ViewModel() {

    private val _films = MutableLiveData<List<FilmSummary>>()
    val films: LiveData<List<FilmSummary>> = _films

    private val _navigationEvents = MutableLiveData<Event<FilmSummary>>()
    val navigationEvents: LiveData<Event<FilmSummary>> = _navigationEvents

    fun onClick(filmSummary: FilmSummary) {
        _navigationEvents.value = Event(filmSummary)
    }

    fun pagedWatchList(): Flow<PagingData<FilmSummary>> {
        val watchListPagingSource = repository.watchList(memberId = "373") // TODO
        val pager = Pager(config = PagingConfig(pageSize = 9)) { watchListPagingSource }
        return pager.flow
    }
}

internal class WatchListViewModelFactory(private val repository: WhatsNextRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = WatchListViewModel(repository) as T
}
