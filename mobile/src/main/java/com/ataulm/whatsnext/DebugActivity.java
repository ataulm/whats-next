package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ataulm.whatsnext.letterboxd.FilmConverter;
import com.ataulm.whatsnext.letterboxd.LetterboxdApi;
import com.ataulm.whatsnext.letterboxd.TokenConverter;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.OkHttpClient;

public class DebugActivity extends AppCompatActivity {

    private WhatsNextService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Clock clock = new Clock();
        TokenConverter tokenConverter = new TokenConverter(clock);
        TokensStore tokensStore = TokensStore.Companion.create(this);
        LetterboxdApi letterboxdApi = new LetterboxdApi(BuildConfig.LETTERBOXD_KEY, BuildConfig.LETTERBOXD_SECRET, clock, tokenConverter, new FilmConverter(), new OkHttpClient(), new Gson());
        service = new WhatsNextService(letterboxdApi, tokensStore, clock);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Film> watchlist = service.watchlist();
                Log.d("!!!", watchlist.toString());
            }
        }).start();
    }

}
