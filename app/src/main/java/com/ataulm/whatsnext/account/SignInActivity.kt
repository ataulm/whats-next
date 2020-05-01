package com.ataulm.whatsnext.account

import android.os.Bundle
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.DaggerSignInComponent
import com.ataulm.whatsnext.di.appComponent
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseActivity() {

    @Inject
    internal lateinit var whatsNextService: WhatsNextService
    private var presenter: SignInPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSignInComponent.builder()
                .appComponent(appComponent())
                .build()
                .inject(this)
        setContentView(R.layout.activity_sign_in)

        val screen = SignInScreen(
                sign_in_edittext_username,
                sign_in_edittext_password,
                sign_in_button,
                sign_in_textview_info
        );
        if (BuildConfig.DEBUG) {
            screen.setCredentials(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD)
        }

        presenter = SignInPresenter(whatsNextService, screen, callback)

    }

    private val callback = SignInPresenter.Callback { username, token ->
        TokensStore.create(this).store(token)
        finish()
    }

    override fun onStart() {
        super.onStart()
        presenter!!.startPresenting()
    }

    override fun onStop() {
        presenter!!.stopPresenting()
        super.onStop()
    }
}