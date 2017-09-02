package com.ataulm.whatsnext.account;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

class SignInScreen {

    private final EditText usernameEditText;
    private final EditText passwordEditText;
    private final Button signInButton;
    private final TextView infoTextView;

    SignInScreen(EditText usernameEditText, EditText passwordEditText, Button signInButton, TextView infoTextView) {
        this.usernameEditText = usernameEditText;
        this.passwordEditText = passwordEditText;
        this.signInButton = signInButton;
        this.infoTextView = infoTextView;
    }

    void attach(final Callback callback) {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                callback.onClickSignIn(username, password);
            }
        });
    }

    void detachCallback() {
        signInButton.setOnClickListener(null);
    }

    void setCredentials(String username, String password) {
        usernameEditText.setText(username);
        passwordEditText.setText(password);
    }

    void showLoading() {
        infoTextView.setText("trying to sign in!");
    }

    void showErrorSigningIn() {
        infoTextView.setText("ah, there was an error signing in. try again later.");
    }

    interface Callback {

        void onClickSignIn(String username, String password);
    }
}
