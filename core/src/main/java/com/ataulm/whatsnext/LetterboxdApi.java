package com.ataulm.whatsnext;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class LetterboxdApi {

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
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/auth/token").build();
        String urlEncodedBody = createUrlEncodedBody(
                new Entry("grant_type", "password"),
                new Entry("username", username),
                new Entry("password", password)
        );
        Response response = createAndExecuteRequest(HTTP_METHOD_POST, url, urlEncodedBody);
        return gson.fromJson(response.body().string(), ApiAuthResponse.class);
    }

    ApiAuthResponse refreshAccessToken(String refreshToken) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/auth/token").build();
        String urlEncodedBody = createUrlEncodedBody(
                new Entry("grant_type", "refresh_token"),
                new Entry("refresh_token", refreshToken)
        );
        Response response = createAndExecuteRequest(HTTP_METHOD_POST, url, urlEncodedBody);
        return gson.fromJson(response.body().string(), ApiAuthResponse.class);
    }

    ApiSearchResponse search(String searchTerm) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/search").addQueryParameter("input", searchTerm).build();
        Response response = createAndExecuteRequest(HTTP_METHOD_GET, url, "");
        return gson.fromJson(response.body().string(), ApiSearchResponse.class);
    }

    ApiMemberAccountResponse me(String accessToken) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/me").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        return gson.fromJson(response.body().string(), ApiMemberAccountResponse.class);
    }

    ApiFilmsResponse watchlist(String accessToken, String userId) throws IOException {
        String url = new LetterboxdUrlBuilder(apiKey, clock).path("/member/" + userId + "/watchlist").build();
        Response response = createAndExecuteUserAuthedRequest(HTTP_METHOD_GET, url, accessToken);
        return gson.fromJson(response.body().string(), ApiFilmsResponse.class);
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
