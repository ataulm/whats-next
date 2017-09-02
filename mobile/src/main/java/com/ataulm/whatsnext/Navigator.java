package com.ataulm.whatsnext;

import android.content.Context;
import android.content.Intent;

import com.ataulm.whatsnext.account.SignInActivity;
import com.ataulm.whatsnext.film.FilmActivity;
import com.ataulm.whatsnext.search.SearchActivity;

public class Navigator {

    private final Context context;

    Navigator(Context context) {
        this.context = context;
    }

    public void navigateToSearch() {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public void navigateToFilm(String letterBoxd) {
        Intent intent = new Intent(context, FilmActivity.class);
        intent.putExtra(FilmActivity.EXTRA_FILM_ID, letterBoxd);
        context.startActivity(intent);
    }

    public void navigateToSignIn() {
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }
}
