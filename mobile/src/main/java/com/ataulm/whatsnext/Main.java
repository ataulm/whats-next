package com.ataulm.whatsnext;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class Main {

    public static void main(String... args) throws IOException {
        Authenticator authenticator = new Authenticator(BuildConfig.LETTERBOXD_KEY, BuildConfig.LETTERBOXD_SECRET, new Clock(), new OkHttpClient(), new Gson());

        ApiAuthResponse apiAuthResponse = authenticator.fetchAccessToken(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
        System.out.println("original access token: " + apiAuthResponse.accessToken);

        ApiAuthResponse refreshedResponse = authenticator.refreshAccessToken(apiAuthResponse.refreshToken);
        System.out.println("refreshed access token: " + refreshedResponse.accessToken);
    }
}
