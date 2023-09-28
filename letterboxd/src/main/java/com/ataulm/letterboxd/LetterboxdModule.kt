package com.ataulm.letterboxd

import android.app.Application
import com.ataulm.letterboxd.auth.AddAuthorizationInterceptor
import com.ataulm.letterboxd.auth.LetterboxdAuthApi
import com.ataulm.letterboxd.auth.LetterboxdAuthenticator
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

private const val LETTERBOXD_BASE_URL = "https://api.letterboxd.com/api/v0/"

@Module
@InstallIn(SingletonComponent::class)
internal interface LetterboxdModule {

    @Binds
    fun bindsLetterboxdRepository(impl: LetterboxdRepositoryImpl): LetterboxdRepository

    companion object {

        // TODO: qualifiers so we can provide main dispatcher too
        @Reusable
        @Provides
        fun providesIoCoroutineContext(): CoroutineContext = Dispatchers.IO

        @Reusable
        @Provides
        fun providesMoshiConverter(): MoshiConverterFactory = MoshiConverterFactory.create()

        @Reusable
        @Provides
        fun providesChuckerInterceptor(application: Application): ChuckerInterceptor =
            ChuckerInterceptor(application)

        @Reusable
        @Provides
        fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

        @Singleton
        @Provides
        fun providesLocalTokensStorage(application: Application): LocalTokensStorage =
            LocalTokensStorage.create(application)

        @Reusable
        @Provides
        fun providesAuthApi(
            moshiConverterFactory: MoshiConverterFactory,
            httpLoggingInterceptor: HttpLoggingInterceptor,
            chuckerInterceptor: ChuckerInterceptor
        ): LetterboxdAuthApi {
            return Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .run {
                            if (BuildConfig.DEBUG) {
                                this.addInterceptor(chuckerInterceptor)
                                    .addNetworkInterceptor(httpLoggingInterceptor)
                            } else {
                                this
                            }
                        }
                        .build()
                )
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(LETTERBOXD_BASE_URL)
                .build()
                .create(LetterboxdAuthApi::class.java)
        }

        @Reusable
        @Provides
        fun providesApi(
            authorizationInterceptor: AddAuthorizationInterceptor,
            authenticator: LetterboxdAuthenticator,
            moshiConverterFactory: MoshiConverterFactory,
            httpLoggingInterceptor: HttpLoggingInterceptor,
            chuckerInterceptor: ChuckerInterceptor
        ): LetterboxdApi {
            return Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .addNetworkInterceptor(authorizationInterceptor)
                        .run {
                            if (BuildConfig.DEBUG) {
                                this.addInterceptor(chuckerInterceptor)
                                    .addNetworkInterceptor(httpLoggingInterceptor)
                            } else {
                                this
                            }
                        }
                        .authenticator(authenticator)
                        .build()
                )
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(LETTERBOXD_BASE_URL)
                .build()
                .create(LetterboxdApi::class.java)
        }
    }
}
