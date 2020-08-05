package com.ataulm.whatsnext

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ataulm.whatsnext.FilmSummaryViewHolder.Companion.inflateView

class FilmSummariesAdapter(
        private val callback: FilmSummaryViewHolder.Callback,
        private val filmSummaries: List<FilmSummary>
) : ListAdapter<FilmSummary, FilmSummaryViewHolder>(Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmSummaryViewHolder {
        return inflateView(parent)
    }

    override fun onBindViewHolder(holder: FilmSummaryViewHolder, position: Int) {
        val filmSummary = filmSummaries[position]
        holder.bind(filmSummary, callback)
    }

    private object Differ : DiffUtil.ItemCallback<FilmSummary>() {
        override fun areItemsTheSame(oldItem: FilmSummary, newItem: FilmSummary): Boolean {
            return oldItem.ids.letterboxd == newItem.ids.letterboxd
        }

        override fun areContentsTheSame(oldItem: FilmSummary, newItem: FilmSummary): Boolean {
            return oldItem == newItem
        }
    }
}
