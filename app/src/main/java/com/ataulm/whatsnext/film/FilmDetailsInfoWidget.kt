package com.ataulm.whatsnext.film

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ataulm.whatsnext.Film
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.merge_film_details_info.view.*

class FilmDetailsInfoWidget constructor(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_film_details_info, this)
    }

    fun bind(film: Film) {
        film.tagline?.let {
            film_details_info_text_tagline.text = it
            film_details_info_text_tagline.visibility = VISIBLE
        } ?: run {
            film_details_info_text_tagline.visibility = GONE
        }

        film.description?.let {
            film_details_info_text_description.text = it
            film_details_info_text_description.visibility = VISIBLE
        } ?: run {
            film_details_info_text_description.visibility = GONE
        }
    }
}
