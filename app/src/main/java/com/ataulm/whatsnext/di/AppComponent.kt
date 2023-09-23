package com.ataulm.whatsnext.di

import android.app.Application
import com.ataulm.letterboxd.LetterboxdComponent
import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.SignInUseCase
import com.ataulm.whatsnext.account.UserIsSignedInUseCase
import com.ataulm.whatsnext.api.FilmConverter
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmStatsConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(
    dependencies = [
        LetterboxdComponent::class
    ],
    modules = [
        AppModule::class
    ]
)
@AppSingletonScope
interface AppComponent {

    fun whatsNextService(): WhatsNextRepository
    fun signInUseCase(): SignInUseCase
    fun isSignedInUseCase(): UserIsSignedInUseCase

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun component(letterboxdComponent: LetterboxdComponent): Builder

        fun build(): AppComponent
    }
}

@Module
object AppModule {

    @JvmStatic
    @Provides
    fun whatsNextService(letterboxdRepository: LetterboxdRepository): WhatsNextRepository {
        return WhatsNextRepository(
            letterboxdRepository = letterboxdRepository,
            filmSummaryConverter = FilmSummaryConverter(),
            filmConverter = FilmConverter(),
            filmRelationshipConverter = FilmRelationshipConverter(),
            filmStatsConverter = FilmStatsConverter()
        )
    }
}
