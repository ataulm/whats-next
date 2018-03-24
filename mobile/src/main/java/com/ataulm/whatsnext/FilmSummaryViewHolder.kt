package com.ataulm.whatsnext

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_film_summary.view.*

class FilmSummaryViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(filmSummary: FilmSummary, callback: Callback) {
        itemView.setOnClickListener { callback.onClick(filmSummary) }
        itemView.contentDescription = filmSummary.name + " (" + filmSummary.year + ")"
        itemView.film_summary_text_name.text = filmSummary.name + " (" + filmSummary.year + ")"
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
