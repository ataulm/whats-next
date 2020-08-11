package com.ataulm.whatsnext;

import android.app.Activity;
import android.content.Intent;

import com.ataulm.whatsnext.account.SignInActivity;
import com.ataulm.whatsnext.film.FilmActivity;
import com.ataulm.whatsnext.search.SearchActivity;

public class Navigator {

    private final Activity activity;

    Navigator(Activity activity) {
        this.activity = activity;
    }

    public void navigateToSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToFilm(FilmSummary filmSummary) {
        Intent intent = new Intent(activity, FilmActivity.class);
        intent.putExtra(FilmActivity.EXTRA_FILM_SUMMARY, filmSummary);
        activity.startActivity(intent);
    }

    public void navigateToSignIn() {
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
    }
}
