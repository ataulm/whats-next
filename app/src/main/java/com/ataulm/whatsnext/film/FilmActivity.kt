package com.ataulm.whatsnext.film

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.runtime.Composable
import coil.load
import com.ataulm.support.DataObserver
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.model.Film
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.bestFor
import com.ataulm.whatsnext.film.FilmViewModel.FilmDetailsUiModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class FilmActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: FilmViewModel

    private lateinit var titleTextView: TextView
    private lateinit var directorsTextView: TextView
    private lateinit var directorsLabelTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var ratingsCountTextView: TextView
    private lateinit var ratingsDistributionWidget: RatingsDistributionWidget
    private lateinit var ratingsTextGroup: View
    private lateinit var posterImageView: ImageView
    private lateinit var durationTextView: TextView
    private lateinit var durationLabelTextView: TextView
    private lateinit var genresTextView: TextView
    private lateinit var genresLabelTextView: TextView
    private lateinit var taglineTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var likeCheckBox: CheckBox
    private lateinit var inWatchlistCheckBox: CheckBox
    private lateinit var watchedCheckBox: CheckBox
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// TODO: start to migrate. There's a codelab here:
//  https://developer.android.com/codelabs/jetpack-compose-migration?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-migration#4
//        setContent {
//            Film()
//        }
        setContentView(R.layout.activity_film)
        titleTextView = findViewById(R.id.titleTextView)
        directorsTextView = findViewById(R.id.directorsTextView)
        directorsLabelTextView = findViewById(R.id.directorsLabelTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        ratingsCountTextView = findViewById(R.id.ratingsCountTextView)
        ratingsDistributionWidget = findViewById(R.id.ratingsDistributionWidget)
        ratingsTextGroup = findViewById(R.id.ratingsTextGroup)
        posterImageView = findViewById(R.id.posterImageView)
        durationTextView = findViewById(R.id.durationTextView)
        durationLabelTextView = findViewById(R.id.durationLabelTextView)
        genresTextView = findViewById(R.id.genresTextView)
        genresLabelTextView = findViewById(R.id.genresLabelTextView)
        taglineTextView = findViewById(R.id.taglineTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        likeCheckBox = findViewById(R.id.likeCheckBox)
        inWatchlistCheckBox = findViewById(R.id.inWatchlistCheckBox)
        watchedCheckBox = findViewById(R.id.watchedCheckBox)
        ratingBar = findViewById(R.id.ratingBar)


        viewModel.filmDetails.observe(this, DataObserver<FilmDetailsUiModel> {
            display(it)
        })
    }

    @Composable
    private fun Film() {
    }

    private fun display(film: FilmDetailsUiModel) {
        titleTextView.text = if (film.releaseYear != null) {
            getString(R.string.title_and_year, film.title, film.releaseYear)
        } else {
            film.title
        }

        val directors = film.directors()
        if (directors != null) {
            directorsTextView.text = directors
            directorsLabelTextView.visibility = View.VISIBLE
            directorsTextView.visibility = View.VISIBLE
        } else {
            directorsLabelTextView.visibility = View.GONE
            directorsTextView.visibility = View.GONE
        }

        if (film.filmStats != null) {
            ratingTextView.text = film.filmStats.rating.stars()
            // same as text but it chooses between star/stars correctly!
            ratingTextView.contentDescription = film.filmStats.rating.starsContentDesc()

            val formattedRatings = DecimalFormat("#,###").format(film.filmStats.counts.ratings)
            ratingsCountTextView.text = getString(R.string.ratings, formattedRatings)
            ratingsCountTextView.contentDescription =
                getString(R.string.ratings_content_description, formattedRatings)

            ratingsDistributionWidget.show(film.filmStats.ratingsHistogram)

            ratingsTextGroup.visibility = View.VISIBLE
            ratingsDistributionWidget.visibility = View.VISIBLE
        } else {
            ratingsTextGroup.visibility = View.INVISIBLE
            ratingsDistributionWidget.visibility = View.INVISIBLE
        }

        posterImageView
            .load(film.poster.bestFor(posterImageView.width)?.url)

        durationText(film.film)?.let {
            durationTextView.text = it
            durationTextView.visibility = View.VISIBLE
            durationLabelTextView.visibility = View.VISIBLE
        } ?: run {
            durationTextView.visibility = View.GONE
            durationLabelTextView.visibility = View.GONE
        }

        genresText(film.film)?.let {
            genresTextView.text = it
            genresTextView.visibility = View.VISIBLE
            genresLabelTextView.visibility = View.VISIBLE
        } ?: run {
            genresTextView.visibility = View.GONE
            genresLabelTextView.visibility = View.GONE
        }

        film.film?.tagline?.let {
            taglineTextView.text = it
            taglineTextView.visibility = View.VISIBLE
        } ?: run {
            taglineTextView.visibility = View.GONE
        }

        film.film?.description?.let {
            descriptionTextView.text = it
            descriptionTextView.visibility = View.VISIBLE
        } ?: run {
            descriptionTextView.visibility = View.GONE
        }

        if (film.filmRelationship != null) {
            likeCheckBox.setOnCheckedChangeListener(null)
            inWatchlistCheckBox.setOnCheckedChangeListener(null)
            watchedCheckBox.setOnCheckedChangeListener(null)
            ratingBar.onRatingBarChangeListener = null

            likeCheckBox.isChecked = film.filmRelationship.liked
            inWatchlistCheckBox.isChecked = film.filmRelationship.inWatchlist
            watchedCheckBox.isChecked = film.filmRelationship.watched
            ratingBar.rating = film.filmRelationship.rating.toFloat()

            likeCheckBox.setOnCheckedChangeListener { _, liked -> viewModel.onClickLiked(liked) }
            inWatchlistCheckBox.setOnCheckedChangeListener { _, inWatchlist ->
                viewModel.onClickInWatchlist(
                    inWatchlist
                )
            }
            watchedCheckBox.setOnCheckedChangeListener { _, watched ->
                viewModel.onClickWatched(
                    watched
                )
            }
            ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
                if (fromUser) {
                    viewModel.onClickRating(rating)
                }
            }

            likeCheckBox.visibility = View.VISIBLE
            inWatchlistCheckBox.visibility = View.VISIBLE
            watchedCheckBox.visibility = View.VISIBLE
            ratingBar.visibility = View.VISIBLE
        } else {
            likeCheckBox.visibility = View.GONE
            inWatchlistCheckBox.visibility = View.GONE
            watchedCheckBox.visibility = View.GONE
            ratingBar.visibility = View.GONE
        }
    }

    private fun FilmDetailsUiModel.directors(): String? {
        if (directors.isEmpty()) return null
        if (directors.size == 1) return directors.first()
        val builder = StringBuilder()
        for (i in directors.indices) {
            val nextPart = when (i) {
                0 -> directors[0]
                directors.size - 1 -> " & ${directors[i]}"
                else -> ", ${directors[i]}"
            }
            builder.append(nextPart)
        }
        return builder.toString()
    }

    private fun Float.stars() = getString(R.string.stars, formattedNumberOfStars())

    private fun Float.starsContentDesc() = resources.getQuantityString(
        R.plurals.stars_content_description,
        toInt(),
        formattedNumberOfStars()
    )

    /**
     * Returns rating as string, formatted X or X.X (e.g. 4 or 4.5)
     */
    private fun Float.formattedNumberOfStars() = if (this == toInt().toFloat()) {
        toInt().toString()
    } else {
        "%.1f".format(this)
    }

    private fun durationText(film: Film?): String? {
        return film?.runtimeMinutes?.let {
            if (TimeUnit.MINUTES.toHours(it.toLong()) > 0) {
                val hours = TimeUnit.MINUTES.toHours(it.toLong())
                val minutes = it.toLong() - TimeUnit.HOURS.toMinutes(hours)
                resources.getString(
                    R.string.film_details_duration_hours_and_minutes_format,
                    hours.toString(),
                    minutes.toString()
                )
            } else {
                resources.getString(R.string.film_details_duration_minutes_format, it.toString())
            }
        }
    }

    private fun genresText(film: Film?): String? {
        if (film == null) return null
        return if (film.genres.isNotEmpty()) {
            film.genres.joinToString(", ")
        } else {
            null
        }
    }

    companion object {

        const val EXTRA_FILM_SUMMARY = BuildConfig.APPLICATION_ID + ".EXTRA_FILM_SUMMARY"
    }
}
