package com.wceng.datastore

import com.wceng.core.datastore.UserPreference
import com.wceng.core.datastore.test.InMemoryDatastore
import com.wceng.model.DarkThemeConfig
import com.wceng.model.LanguagePreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WoPreferencesDataSourceTest {

    private lateinit var subject: WoPreferencesDataSource

    @Before
    fun setUp() {
        subject = WoPreferencesDataSource(InMemoryDatastore(UserPreference.getDefaultInstance()))
    }

    @Test
    fun userLanguageCodeIsExpectedByDefault() = runTest {
        assertEquals(
            defaultOriginalLanguageCode,
            subject.userData.first().languagePreferences.originalLanguageCode
        )
        assertEquals(
            defaultTargetLanguageCode,
            subject.userData.first().languagePreferences.targetLanguageCode
        )
        assertEquals(
            LanguagePreferences(
                originalLanguageCode = defaultOriginalLanguageCode,
                targetLanguageCode = defaultTargetLanguageCode
            ),
            subject.userData.first().languagePreferences
        )
    }

    @Test
    fun originalAndOriginalLanguageCodeIsExpectedWhenSet() = runTest {
        subject.setOriginalLanguageCode("a")

        assertEquals(
            LanguagePreferences(
                originalLanguageCode = "a",
                targetLanguageCode = defaultTargetLanguageCode
            ),
            subject.userData.first().languagePreferences
        )

        subject.setTargetLanguageCode("b")

        assertEquals(
            LanguagePreferences(
                originalLanguageCode = "a",
                targetLanguageCode = "b"
            ),
            subject.userData.first().languagePreferences
        )
    }

    @Test
    fun languagePreferencesIsExpectedWhenIsSet() = runTest {
        subject.setLanguagePreferences(
            LanguagePreferences(
                originalLanguageCode = "a",
                targetLanguageCode = "b"
            )
        )

        assertEquals(
            LanguagePreferences(
                originalLanguageCode = "a",
                targetLanguageCode = "b"
            ),
            subject.userData.first().languagePreferences
        )
    }


    @Test
    fun shouldHideOnBoardingIsFalseByDefault() = runTest {
        assertFalse(subject.userData.first().shouldHideOnboarding)
    }

    @Test
    fun userShouldHideOnBoardingIsTrueWhenSet() = runTest {
        subject.setShouldHideOnboarding(true)
        assertTrue(subject.userData.first().shouldHideOnboarding)
    }

    @Test
    fun userCollectedTranslatesIsEmpty() = runTest {
        assert(subject.userData.first().collectedTranslates.isEmpty())
    }

    @Test
    fun userCollectedTranslates_collectTranslateToggle() = runTest {
        subject.setCollectTranslate("1", true)
        assertTrue(subject.userData.first().collectedTranslates.contains("1"))
        subject.setCollectTranslate("1", false)
        assertTrue(subject.userData.first().collectedTranslates.isEmpty())
    }

    @Test
    fun recentOriginalLanguageCodes_whenHasValues_isDescByPutTime() = runTest {
        subject.setRecentOriginalLanguageCode("a")
        subject.setRecentOriginalLanguageCode("b")
        subject.setRecentOriginalLanguageCode("c")
        subject.setRecentOriginalLanguageCode("d")
        subject.setRecentOriginalLanguageCode("e")
        subject.setRecentOriginalLanguageCode("f")

        assertEquals(
            listOf("f", "e", "d", "c", "b"),
            subject.userData.first().recentOriginalLanguageCodes
        )

        subject.setRecentOriginalLanguageCode("g")

        assertEquals(
            listOf("g", "f", "e", "d", "c"),
            subject.userData.first().recentOriginalLanguageCodes
        )
    }

    @Test
    fun recentOriginalLanguageCodes_whenPutSameCode_isRecentCodeOnFirst() = runTest {
        subject.setRecentOriginalLanguageCode("a")
        subject.setRecentOriginalLanguageCode("b")
        subject.setRecentOriginalLanguageCode("c")

        subject.setRecentOriginalLanguageCode("a")
        assertEquals(
            listOf("a", "c", "b"),
            subject.userData.first().recentOriginalLanguageCodes
        )

        subject.setRecentOriginalLanguageCode("d")
        subject.setRecentOriginalLanguageCode("e")
        subject.setRecentOriginalLanguageCode("f")
        assertEquals(
            listOf("f", "e", "d", "a", "c"),
            subject.userData.first().recentOriginalLanguageCodes
        )
    }

    @Test
    fun recentTargetLanguageCodes_whenHasValues_isDescByPutTime() = runTest {
        subject.setRecentTargetLanguageCode("a")
        subject.setRecentTargetLanguageCode("b")
        subject.setRecentTargetLanguageCode("c")

        assertEquals(
            listOf("c", "b", "a"),
            subject.userData.first().recentTargetLanguageCodes
        )

        subject.setRecentTargetLanguageCode("a")

        assertEquals(
            listOf("a", "c", "b"),
            subject.userData.first().recentTargetLanguageCodes
        )
    }

    @Test
    fun recentTargetLanguageCodes_whenOverMaxNum_sizeIsFive() = runTest {
        subject.setRecentTargetLanguageCode("a")
        subject.setRecentTargetLanguageCode("b")
        subject.setRecentTargetLanguageCode("c")

        subject.setRecentTargetLanguageCode("a")
        assertEquals(
            listOf("a", "c", "b"),
            subject.userData.first().recentTargetLanguageCodes
        )

        subject.setRecentTargetLanguageCode("d")
        subject.setRecentTargetLanguageCode("e")
        subject.setRecentTargetLanguageCode("f")
        assertEquals(
            listOf("f", "e", "d", "a", "c"),
            subject.userData.first().recentTargetLanguageCodes
        )
    }

    @Test
    fun darkThemeConfigIsFollowSystem() = runTest {
        assertEquals(
            DarkThemeConfig.FOLLOW_SYSTEM,
            subject.userData.first().darkThemeConfig
        )
    }

    @Test
    fun darkThemeConfig_updateValue_isUpdated() = runTest {
        subject.setDarkThemeConfig(DarkThemeConfig.DARK)
        assertEquals(
            DarkThemeConfig.DARK,
            subject.userData.first().darkThemeConfig
        )

        subject.setDarkThemeConfig(DarkThemeConfig.LIGHT)

        assertEquals(
            DarkThemeConfig.LIGHT,
            subject.userData.first().darkThemeConfig
        )
    }
}