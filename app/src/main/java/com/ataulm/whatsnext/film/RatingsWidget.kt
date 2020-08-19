package com.ataulm.whatsnext.film

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ataulm.whatsnext.FilmStats
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.merge_ratings_widget.view.*

class RatingsWidget(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_ratings_widget, this)
    }

    fun show(filmStats: FilmStats) {
        ratingTextView.text = resources.getString(R.string.rating, filmStats.rating.roundToOneDp())
        ratingsDistributionWidget.show(filmStats.ratingsHistogram)
    }

    private fun Float.roundToOneDp() = String.format("%.1f", this)
}
