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
import com.ataulm.whatsnext.di.AppComponent;
import com.ataulm.whatsnext.di.AppComponentProvider;
import com.ataulm.whatsnext.di.DaggerAppComponent;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;

public class WhatsNextApplication extends Application implements AppComponentProvider {

    private AppComponent appComponent;
    private WhatsNextService whatsNextService;
    private Letterboxd letterboxd;
    private Clock clock;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.builder().application(this).build();

        Toaster.create(this);
        clock = new Clock();
        TokenConverter tokenConverter = new TokenConverter(clock);
        letterboxd = createLetterboxd(clock, tokenConverter);
        TokensStore tokensStore = TokensStore.Companion.create(this);
        whatsNextService = createWhatsNextService(clock, letterboxd, tokensStore);
    }

    public Letterboxd letterboxd() {
        return letterboxd;
    }

    public WhatsNextService whatsNextService() {
        return whatsNextService;
    }

    public Clock clock() {
        return clock;
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

    @NotNull
    @Override
    public AppComponent provideAppComponent() {
        return appComponent;
    }
}
