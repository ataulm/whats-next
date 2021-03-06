package com.ataulm.whatsnext.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ataulm.support.DataObserver
import com.ataulm.support.EventObserver
import com.ataulm.support.exhaustive
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.DaggerSearchComponent
import com.ataulm.whatsnext.di.appComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModel: SearchViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val navigator = navigator()
    private val filmSummaryCallback = object : FilmSummaryViewHolder.Callback {
        override fun onClick(filmSummary: FilmSummary) {
            viewModel.onClick(filmSummary)
        }
    }
    private val popularFilmsAdapter = PopularFilmsThisWeekAdapter(filmSummaryCallback)
    private val filmSummariesAdapter = FilmSummariesAdapter(filmSummaryCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_search)

        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
        bottomSheetBehavior.isDraggable = false
        searchFieldContainer.doOnLayout { bottomSheetBehavior.peekHeight = searchFieldContainer.height }

        searchRecyclerView.adapter = filmSummariesAdapter
        popularFilmsThisWeekRecyclerView.adapter = popularFilmsAdapter
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val searchTerm = searchEditText.text.toString().trim()
            if (searchTerm.isNotEmpty()) {
                viewModel.onSearch(searchTerm)
            }
            true
        }

        viewModel.signInUiModel.observe(this, DataObserver<SignInUiModel> { signInUiModel ->
            when (signInUiModel) {
                SignInUiModel.SignedIn -> {
                    signInHeaderView.visibility = View.GONE
                }
                SignInUiModel.TryingToSignIn -> {
                    signInHeaderView.update(SignInHeaderView.UiModel.Loading)
                    signInHeaderView.visibility = View.VISIBLE
                }
                is SignInUiModel.RequiresSignIn -> {
                    signInHeaderView.update(SignInHeaderView.UiModel.RequiresSignIn(
                            signInUiModel.onClickSignIn,
                            signInUiModel.onClickRegister,
                            signInUiModel.errorMessage
                    ))
                    signInHeaderView.visibility = View.VISIBLE

                }
            }.exhaustive
        })

        viewModel.films.observe(this, DataObserver<List<FilmSummary>> { filmSummaries ->
            filmSummariesAdapter.submitList(filmSummaries)
            bottomSheetBehavior.isDraggable = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        lifecycleScope.launch {
            viewModel.pagedPopularFilms().collectLatest { popularFilmsAdapter.submitData(it) }
        }

        viewModel.navigationEvents.observe(this, EventObserver { filmSummary ->
            navigator.navigateToFilm(filmSummary)
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

private class PopularFilmsThisWeekAdapter(
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
