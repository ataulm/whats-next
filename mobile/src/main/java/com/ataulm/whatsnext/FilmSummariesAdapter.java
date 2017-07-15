package com.ataulm.whatsnext;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class FilmSummariesAdapter extends RecyclerView.Adapter<FilmSummaryViewHolder> {

    private List<Film> films;

    public FilmSummariesAdapter(List<Film> films) {
        super.setHasStableIds(true);
        this.films = films;
    }

    public void update(List<Film> films) {
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
