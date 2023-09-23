package com.ataulm.whatsnext

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ataulm.whatsnext.FilmSummaryViewHolder.Companion.inflateView
import com.ataulm.whatsnext.model.FilmSummary

class FilmSummariesAdapter(
        private val callback: FilmSummaryViewHolder.Callback
) : ListAdapter<FilmSummary, FilmSummaryViewHolder>(Differ) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmSummaryViewHolder {
        return inflateView(parent)
    }

    override fun onBindViewHolder(holder: FilmSummaryViewHolder, position: Int) {
        val filmSummary = getItem(position)
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
