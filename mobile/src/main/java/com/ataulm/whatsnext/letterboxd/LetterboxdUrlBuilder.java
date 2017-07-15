package com.ataulm.whatsnext.letterboxd;

import com.ataulm.whatsnext.Clock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class LetterboxdUrlBuilder {

    private static final String API_ENDPOINT = "https://api.letterboxd.com/api/v0";

    private final Map<String, String> queryParameters = new HashMap<>();
    private final String apiKey;
    private final Clock clock;

    private String path;

    LetterboxdUrlBuilder(String apiKey, Clock clock) {
        this.apiKey = apiKey;
        this.clock = clock;
    }

    LetterboxdUrlBuilder path(String path) {
        this.path = path;
        return this;
    }

    LetterboxdUrlBuilder addQueryParameter(String key, String value) {
        queryParameters.put(key, value);
        return this;
    }

    String build() {
        addQueryParameter("apikey", apiKey);
        addQueryParameter("nonce", String.valueOf(UUID.randomUUID()));
        addQueryParameter("timestamp", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(clock.getCurrentTimeMillis())));

        boolean firstQueryParam = true;
        StringBuilder builder = new StringBuilder(API_ENDPOINT).append(path).append('?');
        for (Map.Entry<String, String> queryParam : queryParameters.entrySet()) {
            if (firstQueryParam) {
                firstQueryParam = false;
            } else {
                builder.append('&');
            }
            builder.append(urlEncode(queryParam.getKey())).append('=').append(urlEncode(queryParam.getValue()));
        }
        return builder.toString();
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
