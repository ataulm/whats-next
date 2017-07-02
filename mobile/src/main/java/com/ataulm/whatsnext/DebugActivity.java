package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TokenConverter tokenConverter = new TokenConverter(new Clock());
        final TokensStore tokensStore = TokensStore.Companion.create(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LetterboxdApi letterboxdApi = new LetterboxdApi(BuildConfig.LETTERBOXD_KEY, BuildConfig.LETTERBOXD_SECRET, new Clock(), new OkHttpClient(), new Gson());
                    Token token = tokensStore.getToken();
                    if (token == null) {
                        ApiAuthResponse apiAuthResponse = letterboxdApi.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
                        token = tokenConverter.convert(apiAuthResponse);
                        tokensStore.store(token);
                    }
                    String letterboxId = letterboxdApi.me(token.getAccessToken()).member.letterboxId;
                    Log.d("!!!", letterboxdApi.watchlist(token.getAccessToken(), letterboxId).filmSummaries.toString());
                } catch (IOException e) {
                    Log.e("!!!", "error fetching watchlist", e);
                }
            }
        }).start();
    }

    private static class TokenConverter {

        private final Clock clock;

        private TokenConverter(Clock clock) {
            this.clock = clock;
        }

        Token convert(ApiAuthResponse apiAuthResponse) {
            long expiryTime = clock.getCurrentTimeMillis() + TimeUnit.SECONDS.toMillis(apiAuthResponse.secondsUntilExpiry);
            return new Token(apiAuthResponse.accessToken, apiAuthResponse.refreshToken, expiryTime);
        }
    }

}
