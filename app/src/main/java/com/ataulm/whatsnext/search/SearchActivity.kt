package com.ataulm.whatsnext.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.compose.AsyncImage
import com.ataulm.support.DataObserver
import com.ataulm.support.EventObserver
import com.ataulm.support.exhaustive
import com.ataulm.whatsnext.*
import com.ataulm.whatsnext.di.appComponent
import com.ataulm.whatsnext.model.FilmSummary
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SearchActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: SearchViewModel
    private lateinit var composeView: ComposeView
    private lateinit var signInHeaderView: SignInHeaderView

    //    private lateinit var popularFilmsThisWeekRecyclerView: RecyclerView
    private lateinit var searchBottomSheet: View
    private lateinit var searchFieldContainer: View
    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
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
        setContent {
            AsyncImage(
                model = "https://a.ltrbxd.com/resized/film-poster/8/8/7/6/5/0/887650-theater-camp-0-300-0-450-crop.jpg?v=ba9c77cf10",
                contentDescription = null
            )
        }


//        setContentView(R.layout.activity_search)
//
//        composeView = findViewById(R.id.popularFilmsThisWeekCompose)
//        signInHeaderView = findViewById(R.id.signInHeaderView)
////        popularFilmsThisWeekRecyclerView = findViewById(R.id.popularFilmsThisWeekRecyclerView)
//        searchBottomSheet = findViewById(R.id.searchBottomSheet)
//        searchFieldContainer = findViewById(R.id.searchFieldContainer)
//        searchEditText = findViewById(R.id.searchEditText)
//        searchRecyclerView = findViewById(R.id.searchRecyclerView)
//
//        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet)
//        bottomSheetBehavior.isDraggable = false
//        searchFieldContainer.doOnLayout {
//            bottomSheetBehavior.peekHeight = searchFieldContainer.height
//        }
//
//        searchRecyclerView.adapter = filmSummariesAdapter
////        popularFilmsThisWeekRecyclerView.adapter = popularFilmsAdapter
//        searchEditText.setOnEditorActionListener { _, _, _ ->
//            val searchTerm = searchEditText.text.toString().trim()
//            if (searchTerm.isNotEmpty()) {
//                viewModel.onSearch(searchTerm)
//            }
//            true
//        }
//
//        viewModel.signInUiModel.observe(this, DataObserver<SignInUiModel> { signInUiModel ->
//            when (signInUiModel) {
//                SignInUiModel.SignedIn -> {
//                    signInHeaderView.visibility = View.GONE
//                }
//
//                SignInUiModel.TryingToSignIn -> {
//                    signInHeaderView.update(SignInHeaderView.UiModel.Loading)
//                    signInHeaderView.visibility = View.VISIBLE
//                }
//
//                is SignInUiModel.RequiresSignIn -> {
//                    signInHeaderView.update(
//                        SignInHeaderView.UiModel.RequiresSignIn(
//                            signInUiModel.onClickSignIn,
//                            signInUiModel.onClickRegister,
//                            signInUiModel.errorMessage
//                        )
//                    )
//                    signInHeaderView.visibility = View.VISIBLE
//
//                }
//            }.exhaustive
//        })
//
//        viewModel.films.observe(this, DataObserver<List<FilmSummary>> { filmSummaries ->
//            filmSummariesAdapter.submitList(filmSummaries)
//            bottomSheetBehavior.isDraggable = true
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        })
//
//        lifecycleScope.launch {
//            viewModel.pagedPopularFilms().collectLatest { pagingData: PagingData<FilmSummary> ->
//                val popularFilms = flowOf(pagingData)
//                composeView.setContent {
//                    val films = popularFilms.collectAsLazyPagingItems()
//                    LazyColumn {
//                        items(films.itemCount) { index ->
//                            films[index]?.let { filmSummary ->
//                                val label = "${filmSummary.name} (${filmSummary.year})"
//                                Column(
//                                    modifier = Modifier
//                                        .clickable { filmSummaryCallback.onClick(filmSummary) }
//                                        .semantics(mergeDescendants = true) {
//                                            contentDescription = label
//                                        }
//                                ) {
//                                    val imageWidthDp = 96.dp
//                                    val image = with(LocalDensity.current) {
//                                        filmSummary.poster.bestFor(imageWidthDp.roundToPx())
//                                    }
//                                    Timber.d("image: $image")
//                                    AsyncImage(
//                                        model = image,
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .size(
//                                                width = imageWidthDp,
//                                                height = 144.dp
//                                            )
//                                            .background(color = Color.Red),
//                                        onError = {
//                                            Timber.e(it.result.throwable)
//                                        }
//                                    )
//                                    Text(label)
//                                }
//                            }
//                        }
//                    }
//                }
////                popularFilmsAdapter.submitData(it)
//            }
//        }
//
//        viewModel.navigationEvents.observe(this, EventObserver { filmSummary ->
//            navigator.navigateToFilm(filmSummary)
//        })
    }

    @Deprecated("Deprecated in Java")
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
//    DaggerSearchComponent.builder()
//        .activity(this)
//        .appComponent(appComponent())
//        .build()
//        .inject(this)
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
