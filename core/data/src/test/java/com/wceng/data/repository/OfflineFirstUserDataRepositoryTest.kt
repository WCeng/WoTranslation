package com.wceng.data.repository

import com.wceng.core.datastore.UserPreference
import com.wceng.core.datastore.test.InMemoryDatastore
import com.wceng.datastore.WoPreferencesDataSource
import com.wceng.datastore.defaultOriginalLanguageCode
import com.wceng.datastore.defaultTargetLanguageCode
import com.wceng.model.LanguagePreferences
import com.wceng.model.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OfflineFirstUserDataRepositoryTest() {

    private lateinit var woPreferencesDataSource: WoPreferencesDataSource
    private lateinit var subject: OfflineFirstUserDataRepository

    @Before
    fun setUp() {
        woPreferencesDataSource =
            WoPreferencesDataSource(InMemoryDatastore(UserPreference.getDefaultInstance()))
        subject = OfflineFirstUserDataRepository(woPreferencesDataSource)
    }

    @Test
    fun userDataIsCorrect() = runTest {
        assertEquals(
            UserData(
                languagePreferences = LanguagePreferences(
                    defaultOriginalLanguageCode,
                    defaultTargetLanguageCode
                ),
                shouldHideOnboarding = false,
                collectedTranslates = emptySet(),
                recentOriginalLanguageCodes = listOf(),
                recentTargetLanguageCodes = listOf()
            ), subject.userData.first()
        )
    }

    @Test
    fun setOriginalLanguageCode() = runTest {
        subject.setOriginalLanguageCode("ni")

        assertEquals(
            "ni",
            woPreferencesDataSource.userData.map { it.languagePreferences.originalLanguageCode }
                .first()
        )

        subject.setOriginalLanguageCode("no")

        assertEquals(
            "no",
            woPreferencesDataSource.userData.map { it.languagePreferences.originalLanguageCode }
                .first()
        )

        assertEquals(
            woPreferencesDataSource.userData.map { it.languagePreferences.originalLanguageCode }
                .first(),
            subject.userData.map { it.languagePreferences.originalLanguageCode }.first()
        )
    }

    @Test
    fun setTargetLanguageCode() = runTest {
        subject.setTargetLanguageCode("ni")

        assertEquals(
            "ni",
            woPreferencesDataSource.userData.map { it.languagePreferences.targetLanguageCode }
                .first()
        )

        subject.setTargetLanguageCode("no")

        assertEquals(
            "no",
            woPreferencesDataSource.userData.map { it.languagePreferences.targetLanguageCode }
                .first()
        )

        assertEquals(
            woPreferencesDataSource.userData.map { it.languagePreferences.targetLanguageCode }
                .first(),
            subject.userData.map { it.languagePreferences.targetLanguageCode }.first()
        )
    }

    @Test
    fun setShouldHideOnboarding() = runTest {
        subject.setShouldHideOnboarding(true)

        assertTrue(woPreferencesDataSource.userData.map { it.shouldHideOnboarding }.first())

        assertEquals(
            woPreferencesDataSource.userData
                .map { it.shouldHideOnboarding }
                .first(),
            subject.userData
                .map { it.shouldHideOnboarding }
                .first()
        )
    }

    @Test
    fun setCollectTranslate() = runTest {
        subject.setCollectTranslate("1", true)

        assertEquals(
            setOf("1"),
            subject.userData.map { it.collectedTranslates }.first()
        )

        subject.setCollectTranslate("2", true)

        assertEquals(
            setOf("1", "2"),
            subject.userData.map { it.collectedTranslates }.first()
        )

        subject.setCollectTranslate("2", false)

        assertEquals(
            setOf("1"),
            subject.userData.map { it.collectedTranslates }.first()
        )

        assertEquals(
            woPreferencesDataSource.userData.map { it.collectedTranslates }.first(),
            subject.userData.map { it.collectedTranslates }.first()
        )
    }

}