package com.ataulm.whatsnext.account;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ataulm.whatsnext.WhatsNextApplication;

public class LetterboxdAccountService extends Service {

    private LetterboxdAccountAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        WhatsNextApplication whatsNextApplication = (WhatsNextApplication) getApplicationContext();
        this.authenticator = new LetterboxdAccountAuthenticator(this, whatsNextApplication.letterboxd());
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (AccountManager.ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return authenticator.getIBinder();
        }
        return null;
    }
}
