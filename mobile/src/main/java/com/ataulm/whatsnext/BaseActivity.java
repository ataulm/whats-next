package com.ataulm.whatsnext;

import android.support.v7.app.AppCompatActivity;

import com.ataulm.support.Clock;

public abstract class BaseActivity extends AppCompatActivity {

    protected WhatsNextService whatsNextService() {
        return ((WhatsNextApplication) getApplication()).whatsNextService();
    }

    protected Clock clock() {
        return ((WhatsNextApplication) getApplication()).clock();
    }

    protected Navigator navigator() {
        return ((WhatsNextApplication) getApplication()).navigator();
    }
}
