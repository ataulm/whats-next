package com.ataulm.whatsnext.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ataulm.whatsnext.Film;
import com.ataulm.whatsnext.FilmSummariesAdapter;

import java.util.List;

class SearchDisplayer {

    private final EditText searchEditText;
    private final Button searchButton;
    private final RecyclerView resultsRecyclerView;

    SearchDisplayer(EditText searchEditText, Button searchButton, RecyclerView resultsRecyclerView) {
        this.searchEditText = searchEditText;
        this.searchButton = searchButton;
        this.resultsRecyclerView = resultsRecyclerView;
    }

    void attach(final Callback callback) {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString().trim();
                if (searchTerm.isEmpty()) {
                    return;
                }
                callback.onSearch(searchTerm);
            }
        });
    }

    void detachCallback() {
        searchButton.setOnClickListener(null);
    }

    void display(List<Film> films) {
        if (resultsRecyclerView.getLayoutManager() == null) {
            resultsRecyclerView.setLayoutManager(new LinearLayoutManager(resultsRecyclerView.getContext()));
        }

        FilmSummariesAdapter adapter = (FilmSummariesAdapter) resultsRecyclerView.getAdapter();
        if (adapter == null) {
            adapter = new FilmSummariesAdapter(films);
            resultsRecyclerView.setAdapter(adapter);
        } else {
            // TODO: diff utils?
            adapter.update(films);
        }
    }

    interface Callback {

        void onSearch(String searchTerm);
    }
}
