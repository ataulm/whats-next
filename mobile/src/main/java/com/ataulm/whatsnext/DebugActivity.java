package com.ataulm.whatsnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.ButterKnife;

public class DebugActivity extends BaseActivity {

    private Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        RecyclerView recyclerView = ButterKnife.findById(this, R.id.films_recycler_view);
        TextView errorTextView = ButterKnife.findById(this, R.id.films_text_error);
        FilmsDisplayer filmsDisplayer = new FilmsDisplayer(recyclerView, errorTextView);
        presenter = new Presenter(whatsNextService(), filmsDisplayer);
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
