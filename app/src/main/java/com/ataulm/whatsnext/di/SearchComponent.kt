package com.ataulm.whatsnext.di

import com.ataulm.whatsnext.search.SearchActivity
import dagger.Component

@Component(
        dependencies = [AppComponent::class]
)
@FeatureScope
interface SearchComponent {

    fun inject(activity: SearchActivity)

    @Component.Builder
    interface Builder {

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): SearchComponent
    }
}