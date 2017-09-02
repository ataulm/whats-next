package com.ataulm.whatsnext.account;

import android.support.annotation.Nullable;

import com.ataulm.whatsnext.ErrorTrackingDisposableObserver;
import com.ataulm.whatsnext.Token;
import com.ataulm.whatsnext.WhatsNextService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class SignInPresenter {

    private final WhatsNextService service;
    private final SignInScreen screen;
    private final Callback callback;

    @Nullable
    private Disposable disposable;

    SignInPresenter(WhatsNextService service,
                    SignInScreen screen,
                    Callback callback) {
        this.service = service;
        this.screen = screen;
        this.callback = callback;
    }

    void startPresenting() {
        screen.attach(onClickSignInCallback);
    }

    private final SignInScreen.Callback onClickSignInCallback = new SignInScreen.Callback() {
        @Override
        public void onClickSignIn(final String username, String password) {
            disposable = service.login(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            screen.showLoading();
                        }
                    })
                    .subscribeWith(
                            new ErrorTrackingDisposableObserver<Token>() {
                                @Override
                                public void onNext(Token token) {
                                    callback.onTokenReceieved(username, token);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    screen.showErrorSigningIn();
                                }
                            }
                    );
        }
    };

    void stopPresenting() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    interface Callback {

        void onTokenReceieved(String username, Token token);
    }
}
