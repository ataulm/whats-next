package com.ataulm.whatsnext.di;

import android.app.Application
import com.ataulm.support.Clock
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.api.LetterboxdApi
import com.ataulm.whatsnext.api.LetterboxdApiFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(
        modules = [
            AppModule::class
        ]
)
@Singleton
internal interface AppComponent {

    fun whatsNextService(): WhatsNextRepository
    fun tokensStore(): TokensStore

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

@Module
internal object AppModule {

    @JvmStatic
    @Provides
    fun whatsNextService(tokensStore: TokensStore, application: Application): WhatsNextRepository {
        val letterboxdApi = letterboxdApi(application, tokensStore)
        val filmSummaryConverter = FilmSummaryConverter()
        val filmRelationshipConverter = FilmRelationshipConverter()
        return WhatsNextRepository(letterboxdApi, filmSummaryConverter, filmRelationshipConverter)
    }

    private fun letterboxdApi(application: Application, tokensStore: TokensStore): LetterboxdApi {
        return LetterboxdApiFactory(
                apiKey = BuildConfig.LETTERBOXD_KEY,
                apiSecret = BuildConfig.LETTERBOXD_SECRET,
                tokensStore = tokensStore,
                application = application,
                clock = Clock(),
                enableHttpLogging = BuildConfig.DEBUG
        ).createRemote()
    }

    @JvmStatic
    @Provides
    fun tokensStore(application: Application) = TokensStore.create(application)
}
