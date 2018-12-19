package com.ataulm.whatsnext.search

import android.os.Bundle
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.TokensStore
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    private lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val displayer = SearchDisplayer(
                search_edittext,
                search_button,
                search_recycler_view,
                search_button_sign_in
        )
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
}
