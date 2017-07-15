package com.ataulm.whatsnext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

class FilmSummaryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.film_summary_text_name)
    TextView nameTextView;

    static FilmSummaryViewHolder inflateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_film_summary, parent, false);
        return new FilmSummaryViewHolder(view);
    }

    FilmSummaryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(Film film) {
        itemView.setContentDescription(film.getName() + " (" + film.getYear() + ")");
        nameTextView.setText(film.getName() + " (" + film.getYear() + ")");
    }
}
