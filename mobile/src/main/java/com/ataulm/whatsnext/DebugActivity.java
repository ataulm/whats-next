package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ataulm.whatsnext.letterboxd.FilmConverter;
import com.ataulm.whatsnext.letterboxd.LetterboxdApi;
import com.ataulm.whatsnext.letterboxd.TokenConverter;
import com.google.gson.Gson;

import java.util.Collections;
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

    private static class WhatsNextService {

        private final LetterboxdApi letterboxdApi;
        private final TokensStore tokensStore;
        private final Clock clock;

        WhatsNextService(LetterboxdApi letterboxdApi, TokensStore tokensStore, Clock clock) {
            this.letterboxdApi = letterboxdApi;
            this.tokensStore = tokensStore;
            this.clock = clock;
        }

        List<Film> watchlist() {
            try {
                Token token = tokensStore.getToken();
                if (token == null || token.getExpiryMillisSinceEpoch() < clock.getCurrentTimeMillis()) {
                    token = letterboxdApi.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
                    tokensStore.store(token);
                }
                String letterboxId = letterboxdApi.me(token.getAccessToken()).member.letterboxId;
                return letterboxdApi.watchlist(token.getAccessToken(), letterboxId);
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }
    }
}
