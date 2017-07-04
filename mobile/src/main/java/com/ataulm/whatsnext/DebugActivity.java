package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ataulm.whatsnext.letterboxd.FilmConverter;
import com.ataulm.whatsnext.letterboxd.LetterboxdApi;
import com.ataulm.whatsnext.letterboxd.TokenConverter;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class DebugActivity extends AppCompatActivity {

    private Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        WhatsNextService service = createWhatsNextService();
        RecyclerView recyclerView = ButterKnife.findById(this, R.id.films_recycler_view);
        TextView errorTextView = ButterKnife.findById(this, R.id.films_text_error);
        FilmsDisplayer filmsDisplayer = new FilmsDisplayer(recyclerView, errorTextView);
        presenter = new Presenter(service, filmsDisplayer);
    }

    private WhatsNextService createWhatsNextService() {
        Clock clock = new Clock();
        TokenConverter tokenConverter = new TokenConverter(clock);
        TokensStore tokensStore = TokensStore.Companion.create(this);
        LetterboxdApi letterboxdApi = new LetterboxdApi(
                BuildConfig.LETTERBOXD_KEY,
                BuildConfig.LETTERBOXD_SECRET,
                clock,
                tokenConverter,
                new FilmConverter(),
                new OkHttpClient(),
                new Gson()
        );
        return new WhatsNextService(letterboxdApi, tokensStore, clock);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }
}
