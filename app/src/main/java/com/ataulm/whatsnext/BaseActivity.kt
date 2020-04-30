package com.ataulm.whatsnext

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected fun navigator(): Navigator {
        return Navigator(this)
    }
}
