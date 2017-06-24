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

class LetterboxdApi {

    private static final String API_ENDPOINT = "https://api.letterboxd.com/api/v0";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_GET = "GET";

    private final String apiKey;
    private final String apiSecret;
    private final Clock clock;
    private final OkHttpClient okHttpClient;
    private final Gson gson;

    LetterboxdApi(String apiKey, String apiSecret, Clock clock, OkHttpClient okHttpClient, Gson gson) {
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
        Response response = createAndExecuteRequest(HTTP_METHOD_POST, url, urlEncodedBody);
        return gson.fromJson(response.body().string(), ApiAuthResponse.class);
    }

    ApiAuthResponse refreshAccessToken(String refreshToken) throws IOException {
        String url = generateAuthUrl();
        String urlEncodedBody = createUrlEncodedBody(
                new Entry("grant_type", "refresh_token"),
                new Entry("refresh_token", refreshToken)
        );
        Response response = createAndExecuteRequest(HTTP_METHOD_POST, url, urlEncodedBody);
        return gson.fromJson(response.body().string(), ApiAuthResponse.class);
    }

    private String generateAuthUrl() {
        return API_ENDPOINT + "/auth/token?"
                + "apikey=" + apiKey
                + "&nonce=" + generateNonce()
                + "&timestamp=" + generateTimestamp();
    }

    ApiSearchResponse search(String searchTerm) throws IOException {
        String url = generateSearchUrl(searchTerm);
        Response response = createAndExecuteRequest(HTTP_METHOD_GET, url, "");
        return gson.fromJson(response.body().string(), ApiSearchResponse.class);
    }

    private String generateSearchUrl(String searchTerm) {
        return API_ENDPOINT + "/search?"
                + "apikey=" + apiKey
                + "&nonce=" + generateNonce()
                + "&timestamp=" + generateTimestamp()
                + "&input=" + searchTerm;
    }

    ApiMemberAccountResponse me(String accessToken) throws IOException {
        String url = generateMeUrl();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        return gson.fromJson(response.body().string(), ApiMemberAccountResponse.class);
    }

    private String generateMeUrl() {
        return API_ENDPOINT + "/me?"
                + "apikey=" + apiKey
                + "&nonce=" + generateNonce()
                + "&timestamp=" + generateTimestamp();
    }

    ApiFilmsResponse watchlist(String accessToken, String userId) throws IOException {
        String url = generateWatchListUrl(userId);
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        return gson.fromJson(response.body().string(), ApiFilmsResponse.class);
    }

    private String generateWatchListUrl(String userId) {
        return API_ENDPOINT + "/member/" + userId + "/watchlist?"
                + "apikey=" + apiKey
                + "&nonce=" + generateNonce()
                + "&timestamp=" + generateTimestamp();
    }

    private Response createAndExecuteUserAuthedRequest(String httpMethod, String url, String accessToken) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url + "&signature=" + generateSignature(httpMethod, url, ""))
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken);

        Request request = builder.build();
        return okHttpClient.newCall(request).execute();
    }

    private Response createAndExecuteRequest(String httpMethod, String url, String urlEncodedBody) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Signature " + generateSignature(httpMethod, url, urlEncodedBody));

        if (HTTP_METHOD_POST.equals(httpMethod)) {
            builder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), urlEncodedBody));
        }

        Request request = builder.build();
        return okHttpClient.newCall(request).execute();
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

    private String generateSignature(String httpMethod, String url, String body) {
        String preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", httpMethod.toUpperCase(Locale.US), url, body);
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
