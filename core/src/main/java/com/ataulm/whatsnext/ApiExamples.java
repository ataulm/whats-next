package com.ataulm.whatsnext;

import java.io.IOException;

/**
 * All requests require API key and secret
 */
public class ApiExamples {

    private final LetterboxdApi letterboxdApi;
    private final String username;
    private final String password;

    public ApiExamples(LetterboxdApi letterboxdApi, String username, String password) {
        this.letterboxdApi = letterboxdApi;
        this.username = username;
        this.password = password;
    }

    // requires user credentials
    public void login() {
        try {
            ApiAuthResponse apiAuthResponse = letterboxdApi.fetchAccessToken(username, password);
            System.out.println("original access token: " + apiAuthResponse.accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // requires valid (non-expired) refresh token
    public void refreshAccessToken() {
        try {
            ApiAuthResponse apiAuthResponse = letterboxdApi.fetchAccessToken(username, password);
            System.out.println("original access token: " + apiAuthResponse.accessToken);
            ApiAuthResponse refreshedResponse = letterboxdApi.refreshAccessToken(apiAuthResponse.refreshToken);
            System.out.println("refreshed access token: " + refreshedResponse.accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search(String searchTerm) {
        try {
            ApiSearchResponse apiAuthResponse = letterboxdApi.search(searchTerm);
            System.out.println("searchResponse " + apiAuthResponse.searchItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
