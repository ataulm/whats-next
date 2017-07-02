package com.ataulm.whatsnext;

import com.ataulm.whatsnext.letterboxd.LetterboxdApi;

import java.util.Collections;
import java.util.List;

class WhatsNextService {

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
