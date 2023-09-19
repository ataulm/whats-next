package com.ataulm.whatsnext

import android.app.Application
import com.ataulm.support.Toaster
import com.ataulm.whatsnext.di.AppComponent
import com.ataulm.whatsnext.di.AppComponentProvider
import com.ataulm.whatsnext.di.DaggerAppComponent
import timber.log.Timber

class WhatsNextApplication : Application(), AppComponentProvider {
    private var appComponent: AppComponent? = null
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element)?.let { "!!! $it" }
                }
            })
        }
        appComponent = DaggerAppComponent.builder().application(this).build()
        Toaster.create(this)
    }

    override fun provideAppComponent(): AppComponent {
        return appComponent!!
    }
}
