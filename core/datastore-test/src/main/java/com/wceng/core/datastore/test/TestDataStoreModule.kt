package com.wceng.core.datastore.test

import androidx.datastore.core.DataStore
import com.wceng.core.datastore.UserPreference
import com.wceng.datastore.UserPreferenceSerializer
import com.wceng.datastore.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestDataStoreModule {

    @Provides
    @Singleton
    fun provideInMemoryDataStore(
        userPreferenceSerializer: UserPreferenceSerializer
    ): DataStore<UserPreference> {
        return InMemoryDatastore(userPreferenceSerializer.defaultValue)
    }
}