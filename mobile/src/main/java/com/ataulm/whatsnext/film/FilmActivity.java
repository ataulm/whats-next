package com.ataulm.whatsnext.film;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ataulm.whatsnext.BaseActivity;
import com.ataulm.whatsnext.BuildConfig;
import com.ataulm.whatsnext.R;

import butterknife.ButterKnife;

public class FilmActivity extends BaseActivity {

    public static final String EXTRA_FILM_ID = BuildConfig.APPLICATION_ID + ".EXTRA_FILM_ID";

    private FilmPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra(EXTRA_FILM_ID)) {
            throw new IllegalStateException("how you open FilmActivity without a film id?");
        }

        setContentView(R.layout.activity_film);

        ImageView posterImageView = ButterKnife.findById(this, R.id.film_image_poster);
        TextView titleTextView = ButterKnife.findById(this, R.id.film_text_title);
        TextView watchStatusTextView = ButterKnife.findById(this, R.id.film_text_watch_status);
        Button markAsWatchedButton = ButterKnife.findById(this, R.id.film_button_mark_watched);
        Button markAsNotWatchedButton = ButterKnife.findById(this, R.id.film_button_mark_not_watched);

        FilmDisplayer displayer = new FilmDisplayer(posterImageView, titleTextView, watchStatusTextView, markAsWatchedButton, markAsNotWatchedButton);
        String filmId = getIntent().getStringExtra(EXTRA_FILM_ID);
        presenter = new FilmPresenter(whatsNextService(), displayer, filmId);
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
