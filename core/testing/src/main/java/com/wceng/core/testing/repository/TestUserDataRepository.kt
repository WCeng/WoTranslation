package com.wceng.core.testing.repository

import com.wceng.data.repository.UserDataRepository
import com.wceng.datastore.defaultOriginalLanguageCode
import com.wceng.datastore.defaultTargetLanguageCode
import com.wceng.model.DarkThemeConfig
import com.wceng.model.LanguagePreferences
import com.wceng.model.UserData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

val emptyUserData = UserData(
    languagePreferences = LanguagePreferences(
        originalLanguageCode = defaultOriginalLanguageCode,
        targetLanguageCode = defaultTargetLanguageCode
    ),
    shouldHideOnboarding = false,
    collectedTranslates = emptySet(),
    recentTargetLanguageCodes = emptyList(),
    recentOriginalLanguageCodes = emptyList(),
    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM
)

class TestUserDataRepository : UserDataRepository {

    private val _userData: MutableSharedFlow<UserData> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    private val currentUserData: UserData
        get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    override val userData: Flow<UserData> = _userData.filterNotNull()

    override suspend fun setOriginalLanguageCode(code: String) {
        _userData.tryEmit(currentUserData.let { current ->
            current.copy(
                languagePreferences = current.languagePreferences.copy(
                    originalLanguageCode = code
                )
            )
        })
    }

    override suspend fun setTargetLanguageCode(code: String) {
        _userData.tryEmit(currentUserData.let { current ->
            current.copy(
                languagePreferences = current.languagePreferences.copy(
                    targetLanguageCode = code
                )
            )
        })
    }

    override suspend fun setLanguagePreferences(languagePreferences: LanguagePreferences) {
        _userData.tryEmit(currentUserData.copy(languagePreferences = languagePreferences))
    }

    override suspend fun setShouldHideOnboarding(hided: Boolean) {
        _userData.tryEmit(currentUserData.copy(shouldHideOnboarding = hided))
    }

    override suspend fun setCollectTranslate(translateId: String, collected: Boolean) {
        currentUserData.collectedTranslates.let { translateIds ->
            val current = if (collected) {
                translateIds + translateId
            } else {
                translateIds - translateIds
            }

            _userData.tryEmit(currentUserData.copy(collectedTranslates = current))
        }
    }

    override suspend fun setRecentOriginalLanguageCode(languageCode: String) {
        _userData.tryEmit(currentUserData.copy(recentOriginalLanguageCodes = currentUserData.recentOriginalLanguageCodes.let {
            (listOf(languageCode) + it).take(5)
        }))
    }

    override suspend fun setRecentTargetLanguageCode(languageCode: String) {
        _userData.tryEmit(currentUserData.copy(recentTargetLanguageCodes = currentUserData.recentTargetLanguageCodes.let {
            (listOf(languageCode) + it).take(5)
        }))
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        TODO("Not yet implemented")
    }

    fun setUserData(userData: UserData) {
        _userData.tryEmit(userData)
    }
}