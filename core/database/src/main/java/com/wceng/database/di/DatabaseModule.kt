package com.wceng.database.di

import android.content.Context
import androidx.room.Room
import com.wceng.database.WoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Singleton
    @Provides
    fun provideWoDatabase(@ApplicationContext context: Context): WoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WoDatabase::class.java,
            "wo-database.db"
        ).build()
    }

}