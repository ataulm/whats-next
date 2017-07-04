package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.ButterKnife;

public class DebugActivity extends BaseActivity {

    private WatchlistPresenter watchlistPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        RecyclerView recyclerView = ButterKnife.findById(this, R.id.watchlist_recycler_view);
        TextView errorTextView = ButterKnife.findById(this, R.id.watchlist_text_error);
        WatchlistDisplayer watchlistDisplayer = new WatchlistDisplayer(recyclerView, errorTextView);
        watchlistPresenter = new WatchlistPresenter(whatsNextService(), watchlistDisplayer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        watchlistPresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        watchlistPresenter.stopPresenting();
        super.onStop();
    }
}
