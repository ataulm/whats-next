package com.ataulm.whatsnext.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ataulm.support.Event
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.WhatsNextRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class SearchViewModel(private val repository: WhatsNextRepository) : ViewModel() {

    private val _films = MutableLiveData<List<FilmSummary>>()
    val films: LiveData<List<FilmSummary>> = _films

    private val _navigationEvents = MutableLiveData<Event<FilmSummary>>()
    val navigationEvents: LiveData<Event<FilmSummary>> = _navigationEvents

    fun onSearch(searchTerm: String) {
        viewModelScope.launch {
            val results = repository.search(searchTerm)
            _films.value = results
        }
    }

    fun onClick(filmSummary: FilmSummary) {
        _navigationEvents.value = Event(filmSummary)
    }

    fun pagedPopularFilms(): Flow<PagingData<FilmSummary>> {
        val popularFilmsPagingSource = repository.popularFilmsThisWeek()
        val pager = Pager(config = PagingConfig(pageSize = 9)) { popularFilmsPagingSource }
        return pager.flow
    }
}

internal class SearchViewModelFactory(private val repository: WhatsNextRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = SearchViewModel(repository) as T
}
