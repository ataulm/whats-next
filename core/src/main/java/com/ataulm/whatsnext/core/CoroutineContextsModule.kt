package com.ataulm.whatsnext.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
interface CoroutineContextsModule {
    companion object {

        @IoContext
        @Provides
        fun ioContext(): CoroutineContext = Dispatchers.IO

        @MainContext
        @Provides
        fun mainContext(): CoroutineContext = Dispatchers.Main
    }
}
