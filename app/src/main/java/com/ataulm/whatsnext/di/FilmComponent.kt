package com.ataulm.whatsnext.di;

import androidx.lifecycle.ViewModelProviders
import com.ataulm.whatsnext.film.FilmActivity
import com.ataulm.whatsnext.film.FilmViewModel
import com.ataulm.whatsnext.film.FilmViewModelProvider
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Component(
        modules = [
            FilmModule::class
        ],
        dependencies = [AppComponent::class]
)
@FeatureScope
interface FilmComponent {

    fun inject(activity: FilmActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: FilmActivity): Builder

        @BindsInstance
        fun with(@FilmId filmId: String): Builder

        fun appComponent(appComponent: AppComponent): Builder

        fun build(): FilmComponent
    }
}

@Module
internal object FilmModule {

    @JvmStatic
    @Provides
    fun viewModel(activity: FilmActivity, factory: FilmViewModelProvider) = ViewModelProviders.of(activity, factory)
            .get(FilmViewModel::class.java)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FilmId
