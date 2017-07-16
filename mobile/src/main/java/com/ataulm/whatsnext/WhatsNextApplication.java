package com.ataulm.whatsnext;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.ataulm.whatsnext.letterboxd.FilmRelationshipConverter;
import com.ataulm.whatsnext.letterboxd.FilmSummaryConverter;
import com.ataulm.whatsnext.letterboxd.Api;
import com.ataulm.whatsnext.letterboxd.TokenConverter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;

public class WhatsNextApplication extends Application {

    private WhatsNextService whatsNextService;
    private Navigator navigator;

    @Override
    public void onCreate() {
        super.onCreate();
        Toaster.init(this);

        whatsNextService = createWhatsNextService();
        navigator = createNavigator();

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

    public Navigator navigator() {
        return navigator;
    }

    private Navigator createNavigator() {
        return new Navigator(this);
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
                new FilmSummaryConverter(),
                new FilmRelationshipConverter(),
                new OkHttpClient(),
                new Gson()
        );
        return new WhatsNextService(api, tokensStore, clock);
    }

}
