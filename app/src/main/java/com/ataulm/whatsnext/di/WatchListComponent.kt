package com.ataulm.whatsnext.di

import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.watchlist.WatchListActivity
import com.ataulm.whatsnext.watchlist.WatchListViewModel
import com.ataulm.whatsnext.watchlist.WatchListViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
        dependencies = [AppComponent::class],
        modules = [
            WatchListModule::class
        ]
)
@FeatureScope
interface WatchListComponent {

    fun inject(activity: WatchListActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: WatchListActivity): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): WatchListComponent
    }
}

@Module
object WatchListModule {

    @JvmStatic
    @Provides
    fun viewModel(activity: WatchListActivity, whatsNextRepository: WhatsNextRepository): WatchListViewModel {
        val viewModelFactory = WatchListViewModelFactory(whatsNextRepository)
        return ViewModelProvider(activity, viewModelFactory).get(WatchListViewModel::class.java)
    }
}
