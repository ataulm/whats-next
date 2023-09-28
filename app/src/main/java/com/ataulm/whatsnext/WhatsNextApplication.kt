package com.ataulm.whatsnext

import android.app.Application
import com.ataulm.support.Toaster
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WhatsNextApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element)?.let { "!!! $it" }
                }
            })
        }
        Toaster.create(this)
    }
}
