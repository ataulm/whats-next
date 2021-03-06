package com.ataulm.whatsnext.di;

import androidx.lifecycle.ViewModelProvider
import com.ataulm.whatsnext.FilmSummary
import com.ataulm.whatsnext.film.FilmActivity
import com.ataulm.whatsnext.film.FilmViewModel
import com.ataulm.whatsnext.film.FilmViewModelProviderFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
        modules = [
            FilmModule::class
        ],
        dependencies = [AppComponent::class]
)
@FeatureScope
internal interface FilmComponent {

    fun inject(activity: FilmActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: FilmActivity): Builder

        @BindsInstance
        fun with(filmSummary: FilmSummary): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): FilmComponent
    }
}

@Module
internal object FilmModule {

    @JvmStatic
    @Provides
    fun viewModel(activity: FilmActivity, factory: FilmViewModelProviderFactory) = ViewModelProvider(activity, factory)
            .get(FilmViewModel::class.java)
}
