package com.ataulm.whatsnext.film

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import com.ataulm.support.resolveColorAttribute
import com.ataulm.whatsnext.FilmStats.RatingsHistogramBar
import com.ataulm.whatsnext.R
import kotlin.math.min
import kotlin.math.roundToInt

class RatingsDistributionWidget(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val accessHelper = AccessHelper()
    private val paint = Paint().apply {
        color = context.resolveColorAttribute(R.attr.colorOnBackground)
    }

    private var ratingsHistogram: List<RatingsHistogramBar>? = null

    init {
        ViewCompat.setAccessibilityDelegate(this, accessHelper)
    }

    fun show(ratingsHistogram: List<RatingsHistogramBar>) {
        this.ratingsHistogram = ratingsHistogram
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        ratingsHistogram?.let {
            val intervalWidth = width.toFloat() / it.size
            it.forEachIndexed { index, interval ->
                val left = index * intervalWidth
                canvas.drawRect(
                        left + 2,
                        height - (interval.weight * height),
                        left + intervalWidth - 2,
                        height.toFloat(),
                        paint
                )
            }
        }
    }

    override fun dispatchHoverEvent(event: MotionEvent): Boolean {
        return if (accessHelper.dispatchHoverEvent(event)) {
            true
        } else {
            super.dispatchHoverEvent(event)
        }
    }

    private inner class AccessHelper : ExploreByTouchHelper(this) {

        private val intervalBounds = Rect()

        @Suppress("DEPRECATION") // setBoundsInParent is required by [ExploreByTouchHelper]
        override fun onPopulateNodeForVirtualView(virtualViewId: Int, node: AccessibilityNodeInfoCompat) {
            node.className = RatingsDistributionWidget::class.simpleName

            val histogram = this@RatingsDistributionWidget.ratingsHistogram ?: return
            val bar = histogram[virtualViewId]
            val frequency = bar.weight.toNearest5Percent()
            val rating = bar.rating
            node.contentDescription = resources.getString(R.string.rating_bar_content_description, frequency, rating)

            updateBoundsForInterval(virtualViewId, histogram)
            node.setBoundsInParent(intervalBounds)
        }

        private fun Float.toNearest5Percent(): Int {
            val percent = this * 100
            val multiples = percent / 5
            return multiples.roundToInt() * 5
        }

        private fun updateBoundsForInterval(index: Int, histogram: List<RatingsHistogramBar>) {
            val intervalWidth = width.toFloat() / histogram.size
            val left = index * intervalWidth
            intervalBounds.left = (left + 2).roundToInt()
            intervalBounds.top = (height - (histogram[index].weight * height)).roundToInt()
            intervalBounds.right = (left + intervalWidth - 2).roundToInt()
            intervalBounds.bottom = height
        }

        override fun getVirtualViewAt(x: Float, y: Float): Int {
            ratingsHistogram?.let {
                val intervalWidth = width.toFloat() / it.size
                // the `min` is necessary because at the extreme end, `x / intervalWidth` == it.size
                return min((x / intervalWidth).toInt(), it.size - 1)
            }
            return HOST_ID
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>) {
            val numberOfIntervals = ratingsHistogram?.size ?: 0
            for (i in 0 until numberOfIntervals) {
                virtualViewIds.add(i)
            }
        }

        override fun onPerformActionForVirtualView(virtualViewId: Int, action: Int, arguments: Bundle?): Boolean {
            return false
        }
    }
}
