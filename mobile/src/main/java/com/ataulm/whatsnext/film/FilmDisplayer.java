package com.ataulm.whatsnext.film;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ataulm.whatsnext.Film;

class FilmDisplayer {

    private final TextView titleTextView;
    private final TextView watchStatusTextView;
    private final Button markAsWatchedButton;
    private final Button markAsNotWatchedButton;

    FilmDisplayer(TextView titleTextView, TextView watchStatusTextView, Button markAsWatchedButton, Button markAsNotWatchedButton) {
        this.titleTextView = titleTextView;
        this.watchStatusTextView = watchStatusTextView;
        this.markAsWatchedButton = markAsWatchedButton;
        this.markAsNotWatchedButton = markAsNotWatchedButton;
    }

    void attach(final Callback callback) {
        markAsWatchedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickMarkAsWatched();
            }
        });

        markAsNotWatchedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickMarkAsNotWatched();
            }
        });
    }

    void detachCallback() {
        markAsWatchedButton.setOnClickListener(null);
        markAsNotWatchedButton.setOnClickListener(null);
    }

    void display(Film film) {
        titleTextView.setText(film.getSummary().getName());
        watchStatusTextView.setText(film.getRelationship().getWatched() ? "watched" : "not watched");
    }

    interface Callback {

        void onClickMarkAsWatched();

        void onClickMarkAsNotWatched();
    }
}
