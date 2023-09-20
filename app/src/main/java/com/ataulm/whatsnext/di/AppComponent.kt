package com.ataulm.whatsnext.di;

import android.app.Application
import com.ataulm.letterboxd.LetterboxdApi
import com.ataulm.letterboxd.auth.LetterboxdAuthApi
import com.ataulm.whatsnext.AuthRepository
import com.ataulm.whatsnext.BuildConfig
import com.ataulm.whatsnext.LocalTokensStorage
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.account.SignInUseCase
import com.ataulm.whatsnext.account.UserIsSignedInUseCase
import com.ataulm.whatsnext.api.FilmConverter
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmStatsConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import com.ataulm.whatsnext.api.auth.AddAuthorizationInterceptor
import com.ataulm.whatsnext.api.auth.LetterboxdAuthRepository
import com.ataulm.whatsnext.api.auth.LetterboxdAuthenticator
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun signInUseCase(): SignInUseCase
    fun isSignedInUseCase(): UserIsSignedInUseCase

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
    fun moshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @JvmStatic
    @Provides
    fun letterboxdAuthApi(
        application: Application,
        moshiConverterFactory: MoshiConverterFactory
    ): LetterboxdAuthApi {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(application))
                    .addHttpLoggingInterceptor()
                    .build()
            )
            .addConverterFactory(moshiConverterFactory)
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
        moshiConverterFactory: MoshiConverterFactory
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
            .addConverterFactory(moshiConverterFactory)
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
    fun localTokensStorage(application: Application): LocalTokensStorage =
        LocalTokensStorage.create(application)

    @JvmStatic
    @Provides // TODO: this should use @Binds; consider when adding Hilt
    fun authRepository(letterboxdAuthRepository: LetterboxdAuthRepository): AuthRepository =
        letterboxdAuthRepository
}
