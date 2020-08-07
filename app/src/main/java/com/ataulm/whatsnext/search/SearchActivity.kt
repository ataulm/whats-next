package com.ataulm.whatsnext.search

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import com.ataulm.support.DataObserver
import com.ataulm.support.EventObserver
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.DaggerSearchComponent
import com.ataulm.whatsnext.di.appComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModel: SearchViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val navigator = navigator()
    private val filmSummariesAdapter = FilmSummariesAdapter(object : FilmSummaryViewHolder.Callback {
        override fun onClick(filmSummary: FilmSummary) {
            viewModel.onClick(filmSummary)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_search)

        signInButton.setOnClickListener { navigator.navigateToSignIn() }

        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
        searchFieldContainer.doOnLayout { bottomSheetBehavior.peekHeight = searchFieldContainer.height }

        searchRecyclerView.adapter = filmSummariesAdapter
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val searchTerm = searchEditText.text.toString().trim()
            if (searchTerm.isNotEmpty()) {
                viewModel.onSearch(searchTerm)
            }
            true
        }

        viewModel.films.observe(this, DataObserver<List<FilmSummary>> { filmSummaries ->
            filmSummariesAdapter.submitList(filmSummaries)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        viewModel.navigationEvents.observe(this, EventObserver { filmSummary ->
            navigator.navigateToFilm(filmSummary.ids.letterboxd)
        })
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            searchEditText.clearFocus()
        } else {
            super.onBackPressed()
        }
    }
}

private fun SearchActivity.injectDependencies() {
    DaggerSearchComponent.builder()
            .activity(this)
            .appComponent(appComponent())
            .build()
            .inject(this)
}
