package com.ataulm.whatsnext;

import android.app.Application;

import com.ataulm.support.Clock;
import com.ataulm.support.Toaster;
import com.ataulm.whatsnext.api.FakeLetterboxd;
import com.ataulm.whatsnext.api.FilmRelationshipConverter;
import com.ataulm.whatsnext.api.FilmSummaryConverter;
import com.ataulm.whatsnext.api.Letterboxd;
import com.ataulm.whatsnext.api.LetterboxdImpl;
import com.ataulm.whatsnext.api.TokenConverter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;

public class WhatsNextApplication extends Application {

    private WhatsNextService whatsNextService;
    private Navigator navigator;
    private Letterboxd letterboxd;
    private Clock clock;

    @Override
    public void onCreate() {
        super.onCreate();
        Toaster.create(this);

        clock = new Clock();
        TokenConverter tokenConverter = new TokenConverter(clock);
        letterboxd = createLetterboxd(clock, tokenConverter);
        TokensStore tokensStore = TokensStore.Companion.create(this);
        whatsNextService = createWhatsNextService(clock, letterboxd, tokensStore);
        navigator = createNavigator();

        initializeFabric();
    }

    private void initializeFabric() {
        CrashlyticsCore core = new CrashlyticsCore.Builder().build();
        Crashlytics crashlytics = new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics);
    }

    public Letterboxd letterboxd() {
        return letterboxd;
    }

    public WhatsNextService whatsNextService() {
        return whatsNextService;
    }

    public Navigator navigator() {
        return navigator;
    }

    public Clock clock() {
        return clock;
    }

    private Navigator createNavigator() {
        return new Navigator(this);
    }

    private WhatsNextService createWhatsNextService(Clock clock, Letterboxd letterboxd, TokensStore tokensStore) {
        return new WhatsNextService(letterboxd, tokensStore, clock);
    }

    private Letterboxd createLetterboxd(Clock clock, TokenConverter tokenConverter) {
        if (BuildConfig.OFFLINE) {
            return new FakeLetterboxd();
        }

        return new LetterboxdImpl(
                BuildConfig.LETTERBOXD_KEY,
                BuildConfig.LETTERBOXD_SECRET,
                clock,
                tokenConverter,
                new FilmSummaryConverter(),
                new FilmRelationshipConverter(),
                new OkHttpClient(),
                new Gson()
        );
    }
}
