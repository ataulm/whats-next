package com.ataulm.whatsnext.watchlist

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ataulm.support.EventObserver
import com.ataulm.whatsnext.BaseActivity
import com.ataulm.whatsnext.model.FilmSummary
import com.ataulm.whatsnext.FilmSummaryViewHolder
import com.ataulm.whatsnext.R
import com.ataulm.whatsnext.di.appComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class WatchListActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: WatchListViewModel

    private val navigator = navigator()
    private val filmSummaryCallback = object : FilmSummaryViewHolder.Callback {
        override fun onClick(filmSummary: FilmSummary) {
            viewModel.onClick(filmSummary)
        }
    }
    private val watchListAdapter = WatchListAdapter(filmSummaryCallback)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_watch_list)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = watchListAdapter
        lifecycleScope.launch {
            viewModel.pagedWatchList().collectLatest { watchListAdapter.submitData(it) }
        }

        viewModel.navigationEvents.observe(this, EventObserver { filmSummary ->
            navigator.navigateToFilm(filmSummary)
        })
    }
}

private class WatchListAdapter(
        private val callback: FilmSummaryViewHolder.Callback
) : PagingDataAdapter<FilmSummary, FilmSummaryViewHolder>(FilmDiffer) {

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmSummaryViewHolder {
        return FilmSummaryViewHolder.inflateView(parent)
    }

    override fun onBindViewHolder(holder: FilmSummaryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, callback) }
    }

    object FilmDiffer : DiffUtil.ItemCallback<FilmSummary>() {

        override fun areItemsTheSame(oldItem: FilmSummary, newItem: FilmSummary): Boolean {
            return oldItem.ids.letterboxd == newItem.ids.letterboxd
        }

        override fun areContentsTheSame(oldItem: FilmSummary, newItem: FilmSummary): Boolean {
            return oldItem == newItem
        }
    }
}

private fun WatchListActivity.injectDependencies() {
//    DaggerWatchListComponent.builder()
//            .activity(this)
//            .appComponent(appComponent())
//            .build()
//            .inject(this)
}
