package com.ataulm.whatsnext.search;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.FilmSummariesAdapter;
import com.ataulm.whatsnext.FilmSummaryViewHolder;

import java.util.List;

class SearchDisplayer {

    private final EditText searchEditText;
    private final Button searchButton;
    private final RecyclerView resultsRecyclerView;
    private final Button signInButton;

    @Nullable
    private Callback callback;

    SearchDisplayer(EditText searchEditText, Button searchButton, RecyclerView resultsRecyclerView, Button signInButton) {
        this.searchEditText = searchEditText;
        this.searchButton = searchButton;
        this.resultsRecyclerView = resultsRecyclerView;
        this.signInButton = signInButton;
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

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickSignIn();
            }
        });
    }

    void detachCallback() {
        searchButton.setOnClickListener(null);
        signInButton.setOnClickListener(null);
        this.callback = null;
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

    private Toast toast;

    void toastAlreadySignedIn() {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(resultsRecyclerView.getContext(), "already signed in!", Toast.LENGTH_SHORT);
        toast.show();
    }

    interface Callback {

        void onSearch(String searchTerm);

        void onClick(FilmSummary filmSummary);

        void onClickSignIn();
    }
}
