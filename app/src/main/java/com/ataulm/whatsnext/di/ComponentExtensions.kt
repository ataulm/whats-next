package com.ataulm.whatsnext.di

import android.app.Activity

internal fun Activity.appComponent() =
        (applicationContext as? AppComponentProvider)?.provideAppComponent()
                ?: throw IllegalStateException("AppComponentProvider not implemented: $applicationContext")
