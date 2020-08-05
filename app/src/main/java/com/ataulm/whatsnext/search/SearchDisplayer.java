package com.ataulm.whatsnext.search;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ataulm.whatsnext.FilmSummariesAdapter;
import com.ataulm.whatsnext.FilmSummary;
import com.ataulm.whatsnext.FilmSummaryViewHolder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

class SearchDisplayer {

    private final EditText searchEditText;
    private final RecyclerView resultsRecyclerView;
    private final BottomSheetBehavior<View> bottomSheetBehavior;

    @Nullable
    private Callback callback;

    SearchDisplayer(
            EditText searchEditText,
            RecyclerView resultsRecyclerView,
            BottomSheetBehavior<View> bottomSheetBehavior
    ) {
        this.searchEditText = searchEditText;
        this.resultsRecyclerView = resultsRecyclerView;
        this.bottomSheetBehavior = bottomSheetBehavior;
    }

    void attach(final Callback callback) {
        this.callback = callback;
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchTerm = searchEditText.getText().toString().trim();
                if (!searchTerm.isEmpty()) {
                    callback.onSearch(searchTerm);
                }
                return true;
            }
        });
    }

    void detachCallback() {
        searchEditText.setOnEditorActionListener(null);
        this.callback = null;
    }

    void display(List<FilmSummary> filmSummaries) {
        FilmSummariesAdapter adapter = (FilmSummariesAdapter) resultsRecyclerView.getAdapter();
        if (adapter == null) {
            adapter = new FilmSummariesAdapter(filmSummaryCallback, filmSummaries);
            resultsRecyclerView.setAdapter(adapter);
        }
        adapter.submitList(filmSummaries);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
