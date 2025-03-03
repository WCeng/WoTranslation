package com.wceng.database.di

import com.wceng.database.WoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun provideTranslateDao(woDatabase: WoDatabase) = woDatabase.transDao()
}