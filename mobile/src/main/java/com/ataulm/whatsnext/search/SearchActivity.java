package com.ataulm.whatsnext.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.ataulm.whatsnext.BaseActivity;
import com.ataulm.whatsnext.R;

import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    private SearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchEditText = ButterKnife.findById(this, R.id.search_edittext);
        Button searchButton = ButterKnife.findById(this, R.id.search_button);
        RecyclerView resultsRecyclerView = ButterKnife.findById(this, R.id.search_recycler_view);

        SearchDisplayer displayer = new SearchDisplayer(searchEditText, searchButton, resultsRecyclerView);
        presenter = new SearchPresenter(whatsNextService(), displayer);
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
