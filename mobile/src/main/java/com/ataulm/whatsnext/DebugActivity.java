package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LetterboxdApi letterboxdApi = new LetterboxdApi(BuildConfig.LETTERBOXD_KEY, BuildConfig.LETTERBOXD_SECRET, new Clock(), new OkHttpClient(), new Gson());
                    ApiAuthResponse apiAuthResponse = letterboxdApi.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
                    String letterboxId = letterboxdApi.me(apiAuthResponse.accessToken).member.letterboxId;
                    Log.d("!!!", letterboxdApi.watchlist(apiAuthResponse.accessToken, letterboxId).filmSummaries.toString());
                } catch (IOException e) {
                    Log.e("!!!", "error fetching watchlist", e);
                }
            }
        }).start();
    }
}
