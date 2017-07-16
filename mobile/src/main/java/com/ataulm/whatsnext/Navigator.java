package com.ataulm.whatsnext;

import android.content.Context;
import android.content.Intent;

import com.ataulm.whatsnext.film.FilmActivity;

public class Navigator {

    private final Context context;

    Navigator(Context context) {
        this.context = context;
    }

    public void navigateToFilm(String letterBoxd) {
        Intent intent = new Intent(context, FilmActivity.class);
        intent.putExtra(FilmActivity.EXTRA_FILM_ID, letterBoxd);
        context.startActivity(intent);
    }
}
