package com.ataulm.whatsnext.search

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.TokensStore
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    private lateinit var presenter: SearchPresenter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
        searchFieldContainer.doOnLayout { bottomSheetBehavior.peekHeight = searchFieldContainer.height }

        val displayer = SearchDisplayer(searchEditText, searchRecyclerView, bottomSheetBehavior)
        val navigator = navigator()
        presenter = SearchPresenter(whatsNextService(), displayer, TokensStore.create(this), clock(), navigator)
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresenting()
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
