package com.wceng.core.data.test

import com.wceng.core.data.test.repository.FakeLanguageRepository
import com.wceng.core.data.test.repository.FakeTranslateRepository
import com.wceng.core.data.test.repository.FakeUserDataRepository
import com.wceng.data.di.DataModule
import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
internal interface TestDataModule {

    @Binds
    fun bindUserDataRepository(
        fakeUserDataRepository: FakeUserDataRepository
    ): UserDataRepository

    @Binds
    fun bindTranslateRepository(
        fakeTranslateRepository: FakeTranslateRepository
    ): TranslateRepository

    @Binds
    fun bindLanguageRepository(
        fakeLanguageRepository: FakeLanguageRepository
    ): LanguageRepository

    @Binds
    fun bindNetworkMonitor(
        alwaysOnlineNetworkMonitor: AlwaysOnlineNetworkMonitor
    ): NetworkMonitor

}