package com.ataulm.whatsnext;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class FilmsDisplayer {

    private final RecyclerView recyclerView;
    private final TextView errorTextView;

    FilmsDisplayer(RecyclerView recyclerView, TextView errorTextView) {
        this.recyclerView = recyclerView;
        this.errorTextView = errorTextView;
    }

    void displayError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    void display(List<Film> films) {
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }

        FilmsAdapter adapter = (FilmsAdapter) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new FilmsAdapter(films);
            recyclerView.setAdapter(adapter);
        } else {
            // TODO: diff utils?
            adapter.update(films);
        }

        errorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    static class FilmsAdapter extends RecyclerView.Adapter<FilmSummaryViewHolder> {

        private List<Film> films;

        FilmsAdapter(List<Film> films) {
            super.setHasStableIds(true);
            this.films = films;
        }

        void update(List<Film> films) {
            this.films = films;
            notifyDataSetChanged();
        }

        @Override
        public FilmSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return FilmSummaryViewHolder.inflateView(parent);
        }

        @Override
        public void onBindViewHolder(FilmSummaryViewHolder holder, int position) {
            Film film = films.get(position);
            holder.bind(film);
        }

        @Override
        public int getItemCount() {
            return films.size();
        }

        @Override
        public long getItemId(int position) {
            return films.get(position).getId().hashCode();
        }
    }

    static class FilmSummaryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.film_summary_text_name)
        TextView nameTextView;

        @BindView(R.id.film_summary_text_release_year)
        TextView releaseYearTextView;

        static FilmSummaryViewHolder inflateView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_film_summary, parent, false);
            return new FilmSummaryViewHolder(view);
        }

        FilmSummaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Film film) {
            nameTextView.setText(film.getName());
            releaseYearTextView.setText(film.getYear());
        }
    }
}
