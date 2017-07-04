package com.ataulm.whatsnext;

import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected WhatsNextService whatsNextService() {
        return ((WhatsNextApplication) getApplication()).whatsNextService();
    }
}
