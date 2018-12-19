package com.ataulm.whatsnext.film

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.ataulm.whatsnext.Film

class FilmViewModel(val film: LiveData<Film>) : ViewModel()
