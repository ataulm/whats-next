package com.ataulm.whatsnext.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.watchlist.WatchListActivity
import com.ataulm.whatsnext.watchlist.WatchListViewModel
import com.ataulm.whatsnext.watchlist.WatchListViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object WatchListModule {

    @JvmStatic
    @Provides
    fun activity(@ActivityContext context: Context): WatchListActivity {
        return context as WatchListActivity
    }

    @JvmStatic
    @Provides
    fun viewModel(
        activity: WatchListActivity,
        viewModelFactory: WatchListViewModelFactory
    ): WatchListViewModel {
        return ViewModelProvider(activity, viewModelFactory)[WatchListViewModel::class.java]
    }
}
