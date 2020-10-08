package com.ataulm.whatsnext.account

import android.os.Bundle
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.DaggerSignInComponent
import com.ataulm.whatsnext.di.appComponent
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseActivity() {

    @Inject
    internal lateinit var signInUseCase: SignInUseCase

    private lateinit var presenter: SignInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_sign_in)

        val screen = SignInScreen(
                sign_in_edittext_username,
                sign_in_edittext_password,
                sign_in_button,
                sign_in_textview_info
        )

        if (BuildConfig.DEBUG) {
            screen.setCredentials(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD)
        }

        presenter = SignInPresenter(signInUseCase, screen, callback)
    }

    private val callback = object : SignInPresenter.Callback {
        override fun onSignInCompleted() {
            finish()
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}

private fun SignInActivity.injectDependencies() {
    DaggerSignInComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
}
