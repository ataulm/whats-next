package com.ataulm.whatsnext.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ataulm.whatsnext.Film

class FilmViewModel(val film: LiveData<Film>) : ViewModel()
