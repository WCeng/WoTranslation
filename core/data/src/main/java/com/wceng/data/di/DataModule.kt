package com.wceng.data.di

import com.wceng.data.repository.DefaultLanguageRepository
import com.wceng.data.repository.DefaultTranslateRepository
import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.OfflineFirstUserDataRepository
import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.data.util.ConnectivityManagerNetworkMonitor
import com.wceng.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindUserDataRepository(
        offlineFirstUserDataRepository: OfflineFirstUserDataRepository
    ): UserDataRepository

    @Binds
    internal abstract fun bindTranslateRepository(
        defaultTranslateRepository: DefaultTranslateRepository
    ): TranslateRepository

    @Binds
    internal abstract fun bindLanguageRepository(
        defaultLanguageRepository: DefaultLanguageRepository
    ): LanguageRepository

    @Binds
    internal abstract fun bindNetworkMonitor(
        connectivityManagerNetworkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}