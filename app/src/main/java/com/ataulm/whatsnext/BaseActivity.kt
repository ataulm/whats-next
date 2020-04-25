package com.ataulm.whatsnext

import androidx.appcompat.app.AppCompatActivity

import com.ataulm.support.Clock

abstract class BaseActivity : AppCompatActivity() {

    protected fun whatsNextService(): WhatsNextService {
        return (application as WhatsNextApplication).whatsNextService()
    }

    protected fun clock(): Clock {
        return (application as WhatsNextApplication).clock()
    }

    protected fun navigator(): Navigator {
        return Navigator(this)
    }
}
