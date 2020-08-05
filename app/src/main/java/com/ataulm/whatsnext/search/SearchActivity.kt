package com.ataulm.whatsnext.search

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.DaggerSearchComponent
import com.ataulm.whatsnext.di.appComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    @Inject
    internal lateinit var whatsNextService: WhatsNextService
    private lateinit var presenter: SearchPresenter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val filmSummariesAdapter = FilmSummariesAdapter(object : FilmSummaryViewHolder.Callback {
        override fun onClick(filmSummary: FilmSummary) {
            presenter.onClick(filmSummary)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSearchComponent.builder()
                .appComponent(appComponent())
                .build()
                .inject(this)
        setContentView(R.layout.activity_search)

        val navigator = navigator()
        signInButton.setOnClickListener { navigator.navigateToSignIn() }

        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
        searchFieldContainer.doOnLayout { bottomSheetBehavior.peekHeight = searchFieldContainer.height }

        presenter = SearchPresenter(whatsNextService, navigator)
        searchRecyclerView.adapter = filmSummariesAdapter
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val searchTerm = searchEditText.text.toString().trim()
            if (searchTerm.isNotEmpty()) {
                presenter.onSearch(searchTerm) { filmSummaries ->
                    filmSummariesAdapter.submitList(filmSummaries)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            true
        }
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
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
