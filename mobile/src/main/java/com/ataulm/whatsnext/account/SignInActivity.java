package com.ataulm.whatsnext.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ataulm.whatsnext.BaseActivity;
import com.ataulm.whatsnext.BuildConfig;
import com.ataulm.whatsnext.R;
import com.ataulm.whatsnext.Token;
import com.ataulm.whatsnext.TokensStore;

import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity {

    private SignInPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText usernameEditText = ButterKnife.findById(this, R.id.sign_in_edittext_username);
        EditText passwordEditText = ButterKnife.findById(this, R.id.sign_in_edittext_password);
        TextView infoTextView = ButterKnife.findById(this, R.id.sign_in_textview_info);
        Button signInButton = ButterKnife.findById(this, R.id.sign_in_button);

        SignInScreen screen = new SignInScreen(usernameEditText, passwordEditText, signInButton, infoTextView);
        if (BuildConfig.DEBUG) {
            screen.setCredentials(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
        }

        presenter = new SignInPresenter(whatsNextService(), screen, callback);
    }

    private final SignInPresenter.Callback callback = new SignInPresenter.Callback() {
        @Override
        public void onTokenReceieved(String username, Token token) {
            TokensStore tokensStore = TokensStore.create(SignInActivity.this);
            tokensStore.store(token);
            finish();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }
}
