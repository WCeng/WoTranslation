package com.wceng.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher


@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @IoDispatcher
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher() = Dispatchers.Default
}