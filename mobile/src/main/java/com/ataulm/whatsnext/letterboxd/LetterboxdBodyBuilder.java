package com.ataulm.whatsnext.letterboxd;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class LetterboxdBodyBuilder {

    private final Map<String, String> bodyEntries = new HashMap<>();

    LetterboxdBodyBuilder add(String key, String value) {
        bodyEntries.put(key, value);
        return this;
    }

    String build() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : bodyEntries.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(urlEncode(entry.getKey())).append('=').append(urlEncode(entry.getValue()));
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
