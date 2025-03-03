package com.wceng.core.data.test.repository

import com.wceng.data.repository.UserDataRepository
import com.wceng.datastore.WoPreferencesDataSource
import com.wceng.model.LanguagePreferences
import com.wceng.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FakeUserDataRepository @Inject constructor(
    private val dataSource: WoPreferencesDataSource
) : UserDataRepository{

    override val userData: Flow<UserData>
        get() = dataSource.userData

    override suspend fun setOriginalLanguageCode(code: String) {
        dataSource.setOriginalLanguageCode(code)
    }

    override suspend fun setTargetLanguageCode(code: String) {
        dataSource.setTargetLanguageCode(code)
    }

    override suspend fun setLanguagePreferences(languagePreferences: LanguagePreferences) {
        dataSource.setLanguagePreferences(languagePreferences)
    }

    override suspend fun setShouldHideOnboarding(hided: Boolean) {
        dataSource.setShouldHideOnboarding(hided)
    }

    override suspend fun setCollectTranslate(translateId: String, collected: Boolean) {
        dataSource.setCollectTranslate(translateId, collected)
    }

    override suspend fun setRecentOriginalLanguageCode(languageCode: String) {
        dataSource.setRecentOriginalLanguageCode(languageCode)
    }

    override suspend fun setRecentTargetLanguageCode(languageCode: String) {
        dataSource.setRecentTargetLanguageCode(languageCode)
    }
}