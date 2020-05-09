package com.ataulm.whatsnext.di;

import android.app.Application
import android.content.Context
import com.ataulm.support.Clock
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.api.LetterboxdApi
import com.ataulm.whatsnext.api.LetterboxdApiFactory
import dagger.*
import javax.inject.Singleton

@Component(
        modules = [
            AppModule::class
        ]
)
@Singleton
interface AppComponent {

    fun context(): Context

    fun whatsNextService(): WhatsNextService

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

@Module(includes = [AppModule.Declarations::class])
internal object AppModule {

    @JvmStatic
    @Provides
    fun whatsNextService(letterboxdApi: LetterboxdApi): WhatsNextService {
        val filmSummaryConverter = FilmSummaryConverter()
        val filmRelationshipConverter = FilmRelationshipConverter()
        return WhatsNextService(letterboxdApi, filmSummaryConverter, filmRelationshipConverter)
    }

    @JvmStatic
    @Provides
    fun letterboxdApi(application: Application): LetterboxdApi {
        return LetterboxdApiFactory(
                apiKey = BuildConfig.LETTERBOXD_KEY,
                apiSecret = BuildConfig.LETTERBOXD_SECRET,
                tokensStore = TokensStore.create(application),
                clock = Clock(),
                enableHttpLogging = BuildConfig.DEBUG
        ).createRemote()
    }

    @Module
    interface Declarations {

        @Binds
        fun application(application: Application): Application
    }
}
