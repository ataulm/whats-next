package com.ataulm.whatsnext

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class FilmSummaryViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.film_summary_text_name)
    lateinit var nameTextView: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bind(filmSummary: FilmSummary, callback: Callback) {
        itemView.setOnClickListener { callback.onClick(filmSummary) }
        itemView.contentDescription = filmSummary.name + " (" + filmSummary.year + ")"
        nameTextView.text = filmSummary.name + " (" + filmSummary.year + ")"
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
