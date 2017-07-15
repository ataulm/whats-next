package com.ataulm.whatsnext;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class FilmSummariesAdapter extends RecyclerView.Adapter<FilmSummaryViewHolder> {

    private List<FilmSummary> filmSummaries;

    public FilmSummariesAdapter(List<FilmSummary> filmSummaries) {
        super.setHasStableIds(true);
        this.filmSummaries = filmSummaries;
    }

    public void update(List<FilmSummary> filmSummaries) {
        this.filmSummaries = filmSummaries;
        notifyDataSetChanged();
    }

    @Override
    public FilmSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FilmSummaryViewHolder.inflateView(parent);
    }

    @Override
    public void onBindViewHolder(FilmSummaryViewHolder holder, int position) {
        FilmSummary filmSummary = filmSummaries.get(position);
        holder.bind(filmSummary);
    }

    @Override
    public int getItemCount() {
        return filmSummaries.size();
    }

    @Override
    public long getItemId(int position) {
        return filmSummaries.get(position).getId().hashCode();
    }
}
