package com.ataulm.whatsnext.search;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.FilmSummariesAdapter;
import com.ataulm.whatsnext.FilmSummaryViewHolder;

import java.util.List;

class SearchDisplayer {

    private final EditText searchEditText;
    private final Button searchButton;
    private final RecyclerView resultsRecyclerView;

    @Nullable
    private Callback callback;

    SearchDisplayer(EditText searchEditText, Button searchButton, RecyclerView resultsRecyclerView) {
        this.searchEditText = searchEditText;
        this.searchButton = searchButton;
        this.resultsRecyclerView = resultsRecyclerView;
    }

    void attach(final Callback callback) {
        this.callback = callback;
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
        this.callback = null;
        searchButton.setOnClickListener(null);
    }

    void display(List<FilmSummary> filmSummaries) {
        if (resultsRecyclerView.getLayoutManager() == null) {
            resultsRecyclerView.setLayoutManager(new LinearLayoutManager(resultsRecyclerView.getContext()));
        }

        FilmSummariesAdapter adapter = (FilmSummariesAdapter) resultsRecyclerView.getAdapter();
        if (adapter == null) {
            adapter = new FilmSummariesAdapter(filmSummaryCallback, filmSummaries);
            resultsRecyclerView.setAdapter(adapter);
        } else {
            // TODO: diff utils?
            adapter.update(filmSummaries);
        }
    }

    private final FilmSummaryViewHolder.Callback filmSummaryCallback = new FilmSummaryViewHolder.Callback() {
        @Override
        public void onClick(FilmSummary filmSummary) {
            if (callback != null) {
                callback.onClick(filmSummary);
            }
        }
    };

    interface Callback {

        void onSearch(String searchTerm);

        void onClick(FilmSummary filmSummary);
    }
}
