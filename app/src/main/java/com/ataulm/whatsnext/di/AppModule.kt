package com.ataulm.whatsnext.di

import com.ataulm.letterboxd.LetterboxdRepository
import com.ataulm.whatsnext.WhatsNextRepository
import com.ataulm.whatsnext.api.FilmConverter
import com.ataulm.whatsnext.api.FilmRelationshipConverter
import com.ataulm.whatsnext.api.FilmStatsConverter
import com.ataulm.whatsnext.api.FilmSummaryConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
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
