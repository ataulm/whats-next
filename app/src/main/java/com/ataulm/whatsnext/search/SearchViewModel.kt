package com.ataulm.whatsnext.search

import androidx.lifecycle.*
import com.ataulm.support.Event
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.WhatsNextService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class SearchViewModel(private val service: WhatsNextService) : ViewModel() {

    private val _films = MutableLiveData<List<FilmSummary>>()
    val films: LiveData<List<FilmSummary>> = _films

    private val _navigationEvents = MutableLiveData<Event<FilmSummary>>()
    val navigationEvents: LiveData<Event<FilmSummary>> = _navigationEvents

    fun onSearch(searchTerm: String) {
        viewModelScope.launch {
            val results = service.search(searchTerm)
            withContext(Dispatchers.Main) {
                _films.value = results
            }
        }
    }

    fun onClick(filmSummary: FilmSummary) {
        _navigationEvents.value = Event(filmSummary)
    }
}

internal class SearchViewModelFactory(private val service: WhatsNextService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = SearchViewModel(service) as T
}
