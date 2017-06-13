package com.ataulm.whatsnext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class Authenticator {

    private static final String API_ENDPOINT = "https://api.letterboxd.com/api/v0";

    private final String apiKey;
    private final String apiSecret;
    private final Clock clock;

    Authenticator(String apiKey, String apiSecret, Clock clock) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.clock = clock;
    }

    String signIn(String username, String password) throws IOException {
        String url = API_ENDPOINT + "/auth/token?"
                + "apikey=" + apiKey
                + "&nonce=" + generateNonce()
                + "&timestamp=" + generateTimestamp();
        String urlEncodedBody = createUrlEncodedBody(username, password);
        String signature = sign("POST", url, urlEncodedBody);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Signature " + signature)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), urlEncodedBody))
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private String createUrlEncodedBody(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "password");
        body.put("username", username);
        body.put("password", password);
        return urlEncode(body);
    }

    private String sign(String httpMethod, String url, String body) {
        String preHashed = String.format(Locale.US, "%s\u0000%s\u0000%s", httpMethod.toUpperCase(Locale.US), url, body);
        return HmacSha256.generateHash(apiSecret, preHashed).toLowerCase(Locale.US);
    }

    private String urlEncode(Map<String, String> values) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(urlEncode(entry.getKey())).append('=').append(entry.getValue());
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

    private String generateNonce() {
        return String.valueOf(UUID.randomUUID());
    }

    private String generateTimestamp() {
        long currentTimeSecondsSinceEpoch = clock.getCurrentTimeMillis() / 1000;
        return String.valueOf(currentTimeSecondsSinceEpoch);
    }
}
