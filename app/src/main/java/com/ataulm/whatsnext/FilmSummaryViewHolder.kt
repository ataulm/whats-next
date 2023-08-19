package com.ataulm.whatsnext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class FilmSummaryViewHolder internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val titleView = itemView.findViewById<TextView>(R.id.titleView)
    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    fun bind(filmSummary: FilmSummary, callback: Callback) {
        itemView.setOnClickListener { callback.onClick(filmSummary) }
        itemView.contentDescription = filmSummary.name + " (" + filmSummary.year + ")"
        titleView.text = filmSummary.name + " (" + filmSummary.year + ")"
        imageView.load(filmSummary.poster.bestFor(imageView.width)?.url)
    }

    interface Callback {

        fun onClick(filmSummary: FilmSummary)
    }

    companion object {

        @JvmStatic
        fun inflateView(parent: ViewGroup): FilmSummaryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_film_summary, parent, false)
            return FilmSummaryViewHolder(view)
        }
    }
}
