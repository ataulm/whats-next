package com.ataulm.whatsnext;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public class Main {

    public static void main(String... args) {
        LetterboxdApi letterboxdApi = new LetterboxdApi(BuildConfig.LETTERBOXD_KEY, BuildConfig.LETTERBOXD_SECRET, new Clock(), new OkHttpClient(), new Gson());
        ApiExamples examples = new ApiExamples(letterboxdApi, BuildConfig.LETTERBOXD_USERNAME, BuildConfig.LETTERBOXD_PASSWORD);
//        examples.login();
//        examples.refreshAccessToken();
        examples.search("iron");
    }
}
