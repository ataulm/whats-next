package com.ataulm.whatsnext.search

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.di.DaggerSearchComponent
import com.ataulm.whatsnext.di.appComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    @Inject
    internal lateinit var whatsNextService: WhatsNextService
    private lateinit var presenter: SearchPresenter
    private lateinit var displayer: SearchDisplayer
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSearchComponent.builder()
                .appComponent(appComponent())
                .build()
                .inject(this)
        setContentView(R.layout.activity_search)

        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
        searchFieldContainer.doOnLayout { bottomSheetBehavior.peekHeight = searchFieldContainer.height }

        val navigator = navigator()
        presenter = SearchPresenter(whatsNextService, navigator)
        displayer = SearchDisplayer(searchEditText, searchRecyclerView, bottomSheetBehavior)

        signInButton.setOnClickListener { navigator.navigateToSignIn() }
    }

    override fun onStart() {
        super.onStart()
        displayer.attach(object : SearchDisplayer.Callback {
            override fun onClick(filmSummary: FilmSummary) {
                presenter.onClick(filmSummary)
            }

            override fun onSearch(searchTerm: String) {
                presenter.onSearch(searchTerm) { filmSummaries ->
                    displayer.display(filmSummaries)
                }
            }
        })
    }

    override fun onStop() {
        presenter.stopPresenting()
        displayer.detachCallback()
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
