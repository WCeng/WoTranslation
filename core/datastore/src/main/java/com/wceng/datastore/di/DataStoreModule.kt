package com.wceng.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.wceng.common.di.ApplicationScope
import com.wceng.common.di.IoDispatcher
import com.wceng.core.datastore.UserPreference
import com.wceng.datastore.UserPreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferenceDataStore(
        @ApplicationContext context: Context,
        @ApplicationScope coroutineScope: CoroutineScope,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        userPreferenceSerializer: UserPreferenceSerializer
    ): DataStore<UserPreference> {
        return DataStoreFactory.create(
            serializer = userPreferenceSerializer,
            scope = CoroutineScope(coroutineScope.coroutineContext + dispatcher)
        ) {
            context.dataStoreFile("user_preferences.pd")
        }
    }

}