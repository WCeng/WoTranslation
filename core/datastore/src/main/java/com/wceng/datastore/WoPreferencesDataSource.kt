package com.wceng.datastore

import androidx.datastore.core.DataStore
import com.wceng.core.datastore.LanguagePreferences
import com.wceng.core.datastore.UserPreference
import com.wceng.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val defaultOriginalLanguageCode: String = "en"
const val defaultTargetLanguageCode: String = "zh"

class WoPreferencesDataSource @Inject constructor(
    private val userPreference: DataStore<UserPreference>
) {
    val userData = userPreference.data.map { userPreference ->
        UserData(
            languagePreferences = com.wceng.model.LanguagePreferences(
                originalLanguageCode = userPreference.languagePreferences.originalLanguageCode
                    .ifEmpty { defaultOriginalLanguageCode },
                targetLanguageCode = userPreference.languagePreferences.targetLanguageCode
                    .ifEmpty { defaultTargetLanguageCode },
            ),
            shouldHideOnboarding = userPreference.shouldHideOnboarding,
            collectedTranslates = userPreference.collectedTranslateIdsMap.keys,
            recentOriginalLanguageCodes = userPreference.recentOriginalLanguageCodesList,
            recentTargetLanguageCodes = userPreference.recentTargetLanguageCodesList
        )
    }

    suspend fun setOriginalLanguageCode(code: String) {
        userPreference.updateData {
            it.toBuilder()
                .setLanguagePreferences(
                    it.languagePreferences.toBuilder()
                        .setOriginalLanguageCode(code)
                        .build()
                )
                .build()
        }
    }

    suspend fun setTargetLanguageCode(code: String) {
        userPreference.updateData {
            it.toBuilder()
                .setLanguagePreferences(
                    it.languagePreferences.toBuilder()
                        .setTargetLanguageCode(code)
                        .build()
                )
                .build()
        }
    }

    suspend fun setLanguagePreferences(languagePreferences: com.wceng.model.LanguagePreferences) {
        userPreference.updateData {
            it.toBuilder()
                .setLanguagePreferences(
                    LanguagePreferences.newBuilder()
                        .setOriginalLanguageCode(languagePreferences.originalLanguageCode)
                        .setTargetLanguageCode(languagePreferences.targetLanguageCode)
                        .build()
                )
                .build()
        }
    }

    suspend fun setShouldHideOnboarding(hided: Boolean) {
        userPreference.updateData {
            it.toBuilder()
                .setShouldHideOnboarding(hided)
                .build()
        }
    }

    suspend fun setCollectTranslate(translateId: String, collected: Boolean) {
        userPreference.updateData {
            if (collected) {
                it.toBuilder()
                    .putCollectedTranslateIds(translateId, true)
                    .build()
            } else {
                it.toBuilder()
                    .removeCollectedTranslateIds(translateId)
                    .build()
            }

        }
    }

    suspend fun setRecentOriginalLanguageCode(languageCode: String) {
        userPreference.updateData { currentPreferences ->
            val new = mutableListOf(*currentPreferences.recentOriginalLanguageCodesList.toTypedArray())

            new.remove(languageCode)
            new.add(0, languageCode)
            if (new.size > 5) {
                new.subList(5, new.size).clear()
            }
            currentPreferences.toBuilder()
                .clearRecentOriginalLanguageCodes()
                .addAllRecentOriginalLanguageCodes(new)
                .build()
        }
    }

    suspend fun setRecentTargetLanguageCode(languageCode: String) {
        userPreference.updateData { currentPreferences ->
            val new = mutableListOf(*currentPreferences.recentTargetLanguageCodesList.toTypedArray())

            new.remove(languageCode)
            new.add(0, languageCode)
            if (new.size > 5) {
                new.subList(5, new.size).clear()
            }
            currentPreferences.toBuilder()
                .clearRecentTargetLanguageCodes()
                .addAllRecentTargetLanguageCodes(new)
                .build()
        }
    }

}