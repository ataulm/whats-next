package com.ataulm.support

import androidx.lifecycle.Observer

class DataObserver<T>(private val handleData: (T) -> Unit) : Observer<T> {

    override fun onChanged(value: T) {
        handleData(value)
    }
}
