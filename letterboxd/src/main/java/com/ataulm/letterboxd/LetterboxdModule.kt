package com.ataulm.letterboxd

import android.app.Application
import com.ataulm.letterboxd.auth.AddAuthorizationInterceptor
import com.ataulm.letterboxd.auth.LetterboxdAuthApi
import com.ataulm.letterboxd.auth.LetterboxdAuthenticator
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.coroutines.CoroutineContext

private const val LETTERBOXD_BASE_URL = "https://api.letterboxd.com/api/v0/"

@Module
internal interface LetterboxdModule {

    @Binds
    fun bindsLetterboxdRepository(impl: LetterboxdRepositoryImpl): LetterboxdRepository

    companion object {

        // TODO: qualifiers so we can provide main dispatcher too
        @Provides
        fun providesIoCoroutineContext(): CoroutineContext = Dispatchers.IO

        @LetterboxdScope
        @Provides
        fun providesMoshiConverter(): MoshiConverterFactory = MoshiConverterFactory.create()

        @LetterboxdScope
        @Provides
        fun providesChuckerInterceptor(application: Application): ChuckerInterceptor =
            ChuckerInterceptor(application)

        @LetterboxdScope
        @Provides
        fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

        @LetterboxdScope
        @Provides
        fun providesLocalTokensStorage(application: Application): LocalTokensStorage =
            LocalTokensStorage.create(application)

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
