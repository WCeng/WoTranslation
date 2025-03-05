package com.wceng.data.repository

import com.wceng.model.DarkThemeConfig
import com.wceng.model.LanguagePreferences
import com.wceng.model.UserData
import kotlinx.coroutines.flow.Flow


interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setOriginalLanguageCode(code: String)

    suspend fun setTargetLanguageCode(code: String)

    suspend fun setLanguagePreferences(languagePreferences: LanguagePreferences)

    suspend fun setShouldHideOnboarding(hided: Boolean)

    suspend fun setCollectTranslate(translateId: String, collected: Boolean)

    suspend fun setRecentOriginalLanguageCode(languageCode: String)

    suspend fun setRecentTargetLanguageCode(languageCode: String)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

}