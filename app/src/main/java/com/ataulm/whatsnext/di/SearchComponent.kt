package com.ataulm.whatsnext.di

import androidx.lifecycle.ViewModelProviders
import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.search.SearchActivity
import com.ataulm.whatsnext.search.SearchViewModel
import com.ataulm.whatsnext.search.SearchViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
        dependencies = [AppComponent::class],
        modules = [
            SearchModule::class
        ]
)
@FeatureScope
internal interface SearchComponent {

    fun inject(activity: SearchActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: SearchActivity): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): SearchComponent
    }
}

@Module
internal object SearchModule {

    @JvmStatic
    @Provides
    fun viewModel(activity: SearchActivity, whatsNextService: WhatsNextService): SearchViewModel {
        val viewModelFactory = SearchViewModelFactory(whatsNextService)
        return ViewModelProviders.of(activity, viewModelFactory).get(SearchViewModel::class.java)
    }
}
