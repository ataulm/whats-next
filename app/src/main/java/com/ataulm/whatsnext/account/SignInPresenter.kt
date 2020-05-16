package com.ataulm.whatsnext.account

import com.ataulm.whatsnext.ErrorTrackingDisposableObserver
import com.ataulm.whatsnext.Token
import com.ataulm.whatsnext.WhatsNextService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

internal class SignInPresenter(
        private val service: WhatsNextService,
        private val screen: SignInScreen,
        private val callback: Callback
) {

    private var disposable: Disposable? = null

    fun startPresenting() {
        screen.attach(onClickSignInCallback)
    }

    private val onClickSignInCallback = SignInScreen.Callback { username, password ->
        disposable = service.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { screen.showLoading() }
                .subscribeWith(
                        object : ErrorTrackingDisposableObserver<Token>() {
                            override fun onNext(token: Token) {
                                callback.onTokenReceieved(username, token)
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                screen.showErrorSigningIn()
                            }
                        }
                )
    }

    fun stopPresenting() {
        disposable?.dispose()
    }

    interface Callback {
        fun onTokenReceieved(username: String, token: Token)
    }
}
