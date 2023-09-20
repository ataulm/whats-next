package com.ataulm.whatsnext;

import android.app.Activity;
import android.content.Intent;

import com.ataulm.whatsnext.film.FilmActivity;
import com.ataulm.whatsnext.model.FilmSummary;
import com.ataulm.whatsnext.watchlist.WatchListActivity;

public class Navigator {

    private final Activity activity;

    Navigator(Activity activity) {
        this.activity = activity;
    }

    public void navigateToFilm(FilmSummary filmSummary) {
        Intent intent = new Intent(activity, FilmActivity.class);
        intent.putExtra(FilmActivity.EXTRA_FILM_SUMMARY, filmSummary);
        activity.startActivity(intent);
    }

    public void navigateToWatchList() {
        Intent intent = new Intent(activity, WatchListActivity.class);
        activity.startActivity(intent);
    }
}
