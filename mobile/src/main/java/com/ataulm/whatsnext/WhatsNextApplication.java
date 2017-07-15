package com.ataulm.whatsnext;

import android.app.Application;

import com.ataulm.whatsnext.letterboxd.FilmConverter;
import com.ataulm.whatsnext.letterboxd.Api;
import com.ataulm.whatsnext.letterboxd.TokenConverter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;

public class WhatsNextApplication extends Application {

    private WhatsNextService whatsNextService;

    @Override
    public void onCreate() {
        super.onCreate();
        whatsNextService = createWhatsNextService();

        initializeFabric();
    }

    private void initializeFabric() {
        CrashlyticsCore core = new CrashlyticsCore.Builder().build();
        Crashlytics crashlytics = new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics);
    }

    public WhatsNextService whatsNextService() {
        return whatsNextService;
    }

    private WhatsNextService createWhatsNextService() {
        Clock clock = new Clock();
        TokenConverter tokenConverter = new TokenConverter(clock);
        TokensStore tokensStore = TokensStore.Companion.create(this);
        Api api = new Api(
                BuildConfig.LETTERBOXD_KEY,
                BuildConfig.LETTERBOXD_SECRET,
                clock,
                tokenConverter,
                new FilmConverter(),
                new OkHttpClient(),
                new Gson()
        );
        return new WhatsNextService(api, tokensStore, clock);
    }
}
