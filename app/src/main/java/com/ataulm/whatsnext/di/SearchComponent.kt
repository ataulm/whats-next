package com.ataulm.whatsnext.di

import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.search.SearchActivity
import com.ataulm.whatsnext.search.SearchPresenter
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

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): SearchComponent
    }
}

@Module
internal object SearchModule {

    @JvmStatic
    @Provides
    fun searchPresenter(whatsNextService: WhatsNextService): SearchPresenter {
        return SearchPresenter(whatsNextService)
    }
}
