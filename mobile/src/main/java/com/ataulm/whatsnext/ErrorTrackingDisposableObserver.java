package com.ataulm.whatsnext;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.reactivex.observers.DisposableObserver;

abstract class ErrorTrackingDisposableObserver<T> extends DisposableObserver<T> {

    @Override
    public void onError(Throwable e) {
        Log.e("!!!", "onError", e);
        Crashlytics.logException(e);
    }
}
