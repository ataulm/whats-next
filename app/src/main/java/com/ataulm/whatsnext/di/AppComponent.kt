package com.ataulm.whatsnext.di;

import android.app.Application
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.SharedPrefsTokensStore
import com.ataulm.whatsnext.TokensStore
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.IsSignedInUseCase
import com.ataulm.whatsnext.account.SignInUseCase
import com.ataulm.whatsnext.api.FilmConverter
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmStatsConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.api.LetterboxdApi
import com.ataulm.whatsnext.api.auth.AddAuthorizationInterceptor
import com.ataulm.whatsnext.api.auth.LetterboxdAuthApi
import com.ataulm.whatsnext.api.auth.LetterboxdAuthenticator
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val LETTERBOXD_BASE_URL = "https://api.letterboxd.com/api/v0/"

@Component(
    modules = [
        AppModule::class
    ]
)
@Singleton
interface AppComponent {

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
object AppModule {

    @JvmStatic
    @Provides
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @JvmStatic
    @Provides
    fun letterboxdAuthApi(
        application: Application,
        gsonConverterFactory: GsonConverterFactory
    ): LetterboxdAuthApi {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(application))
                    .addHttpLoggingInterceptor()
                    .build()
            )
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(LETTERBOXD_BASE_URL)
            .build()
            .create(LetterboxdAuthApi::class.java)
    }

    private fun OkHttpClient.Builder.addHttpLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addNetworkInterceptor(httpLoggingInterceptor)
        }
        return this
    }

    @JvmStatic
    @Provides
    fun letterboxdApi(
        application: Application,
        authorizationInterceptor: AddAuthorizationInterceptor,
        authenticator: LetterboxdAuthenticator,
        gsonConverterFactory: GsonConverterFactory
    ): LetterboxdApi {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(authorizationInterceptor)
                    .addInterceptor(ChuckerInterceptor(application))
                    .addHttpLoggingInterceptor()
                    .authenticator(authenticator)
                    .build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(LETTERBOXD_BASE_URL)
            .build()
            .create(LetterboxdApi::class.java)
    }

    @JvmStatic
    @Provides
    fun whatsNextService(letterboxdApi: LetterboxdApi): WhatsNextRepository {
        return WhatsNextRepository(
            letterboxdApi = letterboxdApi,
            filmSummaryConverter = FilmSummaryConverter(),
            filmConverter = FilmConverter(),
            filmRelationshipConverter = FilmRelationshipConverter(),
            filmStatsConverter = FilmStatsConverter()
        )
    }

    @JvmStatic
    @Provides
    fun tokensStore(application: Application): TokensStore =
        SharedPrefsTokensStore.create(application)
}
