package com.ataulm.whatsnext;

import android.util.Log;

import io.reactivex.observers.DisposableObserver;

public abstract class ErrorTrackingDisposableObserver<T> extends DisposableObserver<T> {

    @Override
    public void onError(Throwable e) {
        Log.e("!!!", "onError", e);
    }

    @Override
    public void onComplete() {
        Log.d("!!!", "onComplete");
    }
}
