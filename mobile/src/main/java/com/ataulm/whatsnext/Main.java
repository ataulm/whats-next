package com.ataulm.whatsnext;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {
        Authenticator authenticator = new Authenticator(BuildConfig.LETTERBOXD_KEY, BuildConfig.LETTERBOXD_SECRET, new Clock());
        String token = authenticator.signIn(BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
        System.out.println("!!! got token response: " + token);
    }
}
