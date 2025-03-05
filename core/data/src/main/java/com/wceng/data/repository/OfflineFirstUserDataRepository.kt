package com.wceng.data.repository

import com.wceng.datastore.WoPreferencesDataSource
import com.wceng.model.DarkThemeConfig
import com.wceng.model.LanguagePreferences
import com.wceng.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class OfflineFirstUserDataRepository @Inject constructor(
    private val datasource: WoPreferencesDataSource
) : UserDataRepository {
    override val userData: Flow<UserData>
        get() = datasource.userData

    override suspend fun setOriginalLanguageCode(code: String) {
        datasource.setOriginalLanguageCode(code)
    }

    override suspend fun setTargetLanguageCode(code: String) {
        datasource.setTargetLanguageCode(code)
    }

    override suspend fun setLanguagePreferences(languagePreferences: LanguagePreferences) {
        datasource.setLanguagePreferences(languagePreferences)
    }

    override suspend fun setShouldHideOnboarding(hided: Boolean) {
        datasource.setShouldHideOnboarding(hided)
    }

    override suspend fun setCollectTranslate(translateId: String, collected: Boolean) {
        datasource.setCollectTranslate(translateId, collected)
    }

    override suspend fun setRecentOriginalLanguageCode(languageCode: String) {
        datasource.setRecentOriginalLanguageCode(languageCode)
    }

    override suspend fun setRecentTargetLanguageCode(languageCode: String) {
        datasource.setRecentTargetLanguageCode(languageCode)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        datasource.setDarkThemeConfig(darkThemeConfig)
    }
}