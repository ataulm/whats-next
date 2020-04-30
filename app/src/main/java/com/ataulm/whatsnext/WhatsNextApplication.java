package com.ataulm.whatsnext;

import android.app.Application;

import com.ataulm.support.Toaster;
import com.ataulm.whatsnext.di.AppComponent;
import com.ataulm.whatsnext.di.AppComponentProvider;
import com.ataulm.whatsnext.di.DaggerAppComponent;

import org.jetbrains.annotations.NotNull;

public class WhatsNextApplication extends Application implements AppComponentProvider {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.builder().application(this).build();

        Toaster.create(this);
    }

    @NotNull
    @Override
    public AppComponent provideAppComponent() {
        return appComponent;
    }
}
