package com.ataulm.whatsnext;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Authenticator {

    private static final String API_ENDPOINT = "https://api.letterboxd.com/api/v0";
    private static final String HTTP_METHOD = "POST";

    private final String apiKey;
    private final String apiSecret;
    private final Clock clock;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    Authenticator(String apiKey, String apiSecret, Clock clock, OkHttpClient okHttpClient, Gson gson) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.clock = clock;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
    }

    ApiAuthResponse fetchAccessToken(String username, String password) throws IOException {
        String url = generateAuthUrl();
        String urlEncodedBody = createUrlEncodedBody(
                new Entry("grant_type", "password"),
                new Entry("username", username),
                new Entry("password", password)
        );
        return createAndExecuteRequest(url, urlEncodedBody);
    }

    ApiAuthResponse refreshAccessToken(String refreshToken) throws IOException {
        String url = generateAuthUrl();
        String urlEncodedBody = createUrlEncodedBody(
                new Entry("grant_type", "refresh_token"),
                new Entry("refresh_token", refreshToken)
        );
        return createAndExecuteRequest(url, urlEncodedBody);
    }

    private ApiAuthResponse createAndExecuteRequest(String url, String urlEncodedBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Signature " + generateSignature(url, urlEncodedBody))
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), urlEncodedBody))
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return gson.fromJson(response.body().string(), ApiAuthResponse.class);
    }

    private String generateAuthUrl() {
        return API_ENDPOINT + "/auth/token?"
                + "apikey=" + apiKey
                + "&nonce=" + generateNonce()
                + "&timestamp=" + generateTimestamp();
    }

    private String generateNonce() {
        return String.valueOf(UUID.randomUUID());
    }

    private String generateTimestamp() {
        long currentTimeSecondsSinceEpoch = clock.getCurrentTimeMillis() / 1000;
        return String.valueOf(currentTimeSecondsSinceEpoch);
    }

    private String createUrlEncodedBody(Entry... entries) {
        StringBuilder builder = new StringBuilder();
        for (Entry entry : entries) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(urlEncode(entry.key)).append('=').append(urlEncode(entry.value));
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

    private String generateSignature(String url, String body) {
        String preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", HTTP_METHOD.toUpperCase(Locale.US), url, body);
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US);
    }

    private static class Entry {
        final String key;
        final String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
