package com.ataulm.whatsnext.film

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ataulm.whatsnext.model.FilmStats
import com.ataulm.whatsnext.R

class RatingsWidget(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var ratingTextView: TextView
    private lateinit var ratingsDistributionWidget: RatingsDistributionWidget

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_ratings_widget, this)
        ratingTextView = findViewById(R.id.ratingTextView)
        ratingsDistributionWidget = findViewById(R.id.ratingsDistributionWidget)
    }

    fun show(filmStats: FilmStats) {
        val rating = filmStats.rating.toNumberStars()
        ratingTextView.text = resources.getString(R.string.stars, rating)
        ratingTextView.contentDescription = resources.getQuantityString(
            R.plurals.stars_content_description,
            filmStats.rating.toInt(),
            rating
        )
        ratingsDistributionWidget.show(filmStats.ratingsHistogram)
    }

    private fun Float.toNumberStars() = if (this == this.toInt().toFloat()) {
        this.toInt().toString()
    } else {
        "%.1f".format(this)
    }
}
