package com.wceng.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope


@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationCoroutineScope(
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ) =
        CoroutineScope(SupervisorJob() + dispatcher)

}