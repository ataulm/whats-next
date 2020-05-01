package com.ataulm.whatsnext.di;

import android.app.Application
import android.content.Context
import com.ataulm.support.Clock
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextService
import com.ataulm.whatsnext.api.*
import com.google.gson.Gson
import dagger.*
import okhttp3.OkHttpClient
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
    fun whatsNextService(context: Context, letterboxdApi: LetterboxdApi): WhatsNextService {
        val clock = Clock()
        val tokenConverter = TokenConverter(clock)
        val letterboxd = createLetterboxd(clock, tokenConverter)
        val tokensStore = TokensStore.create(context)
        return WhatsNextService(letterboxd, letterboxdApi, tokensStore, clock)
    }

    @JvmStatic
    @Provides
    fun letterboxdApi(): LetterboxdApi {
        // TODO: an offline version? if BuildConfig.OFFLINE
        return LetterboxdApiFactory(
                apiKey = BuildConfig.LETTERBOXD_KEY,
                apiSecret = BuildConfig.LETTERBOXD_SECRET,
                clock = Clock(),
                enableHttpLogging = BuildConfig.DEBUG
        ).createRemote()
    }

    private fun createLetterboxd(clock: Clock, tokenConverter: TokenConverter): Letterboxd {
        return if (BuildConfig.OFFLINE) {
            FakeLetterboxd()
        } else LetterboxdImpl(
                BuildConfig.LETTERBOXD_KEY,
                BuildConfig.LETTERBOXD_SECRET,
                clock,
                tokenConverter,
                FilmSummaryConverter(),
                FilmRelationshipConverter(),
                OkHttpClient(),
                Gson()
        )
    }

    @Module
    interface Declarations {

        @Binds
        fun context(application: Application): Context
    }
}
