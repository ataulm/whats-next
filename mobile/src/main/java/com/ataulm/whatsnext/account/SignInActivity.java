package com.ataulm.whatsnext.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ataulm.whatsnext.BaseActivity;
import com.ataulm.whatsnext.BuildConfig;
import com.ataulm.whatsnext.ErrorTrackingDisposableObserver;
import com.ataulm.whatsnext.R;
import com.ataulm.whatsnext.Token;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.sign_in_edittext_username)
    EditText usernameEditText;

    @BindView(R.id.sign_in_edittext_password)
    EditText passwordEditText;

    @BindView(R.id.sign_in_button)
    Button signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            usernameEditText.setText(BuildConfig.LETTERBOXD_USERNAME);
            passwordEditText.setText(BuildConfig.LETTERBOXD_PASSWORD);
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                whatsNextService().login(username, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ErrorTrackingDisposableObserver<Token>() {
                            @Override
                            public void onNext(Token token) {
                                Account account = new Account(username, getString(R.string.account_type));
                                AccountManager.get(getApplicationContext()).addAccountExplicitly(account, null, Bundle.EMPTY);
                                // TODO: next activity, finish this one
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                // TODO: display error and stop loading
                            }
                        });
            }
        });
    }
}
