package com.ataulm.whatsnext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.view_film_summary.view.*

class FilmSummaryViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.imageView.clipToOutline = true
        itemView.backgroundView.clipToOutline = true
    }

    fun bind(filmSummary: FilmSummary, callback: Callback) {
        itemView.setOnClickListener { callback.onClick(filmSummary) }
        itemView.contentDescription = filmSummary.name + " (" + filmSummary.year + ")"
        itemView.titleView.text = filmSummary.name + " (" + filmSummary.year + ")"
        itemView.imageView.load(filmSummary.poster.bestFor(itemView.imageView.width)?.url)
    }

    interface Callback {

        fun onClick(filmSummary: FilmSummary)
    }

    companion object {

        @JvmStatic
        fun inflateView(parent: ViewGroup): FilmSummaryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_film_summary, parent, false)
            return FilmSummaryViewHolder(view)
        }
    }
}
