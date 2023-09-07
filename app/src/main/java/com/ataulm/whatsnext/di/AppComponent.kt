package com.ataulm.whatsnext.di;

import android.app.Application
import com.ataulm.support.Clock
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.SharedPrefsTokensStore
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.IsSignedInUseCase
import com.ataulm.whatsnext.account.SignInUseCase
import com.ataulm.whatsnext.api.*
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
    fun signInUseCase(): SignInUseCase
    fun isSignedInUseCase(): IsSignedInUseCase

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
    fun whatsNextService(
        tokensStore: TokensStore,
        application: Application
    ): WhatsNextRepository {
        return WhatsNextRepository(
            letterboxdApi = letterboxdApi(application, tokensStore),
            letterboxdAuthApi = letterboxdAuthApi(application, tokensStore),
            filmSummaryConverter = FilmSummaryConverter(),
            filmConverter = FilmConverter(),
            filmRelationshipConverter = FilmRelationshipConverter(),
            filmStatsConverter = FilmStatsConverter()
        )
    }

    // TODO: creating multiple Api Factories, nicer way to structure this
    private fun letterboxdAuthApi(
        application: Application,
        tokensStore: TokensStore
    ): LetterboxdAuthApi {
        return LetterboxdApiFactory(
            apiKey = BuildConfig.LETTERBOXD_KEY,
            apiSecret = BuildConfig.LETTERBOXD_SECRET,
            tokensStore = tokensStore,
            application = application,
            clock = Clock(),
            enableHttpLogging = BuildConfig.DEBUG
        ).createAuthApi()
    }

    private fun letterboxdApi(
        application: Application,
        tokensStore: TokensStore
    ): LetterboxdApi {
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
    fun tokensStore(application: Application): TokensStore =
        SharedPrefsTokensStore.create(application)

    @JvmStatic
    @Provides
    fun signInUseCase(
        whatsNextRepository: WhatsNextRepository,
        tokensStore: TokensStore
    ) =
        SignInUseCase(whatsNextRepository, tokensStore)

    @JvmStatic
    @Provides
    fun isSignedInUseCase(tokensStore: TokensStore) = IsSignedInUseCase(tokensStore)
}
