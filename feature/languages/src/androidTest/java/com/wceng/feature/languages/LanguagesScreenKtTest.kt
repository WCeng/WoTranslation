package com.wceng.feature.languages

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wceng.core.testing.data.defaultAutoLanguage
import com.wceng.core.testing.data.languagesTestData
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguagesScreenKtTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)


    @Test
    fun whenIsOriginalLanguage_topAppbarTitleIsOriginalLanguage() {
        composeTestRule.setContent {
            LanguagesScreen(
                isOriginalLanguages = true,
                autoDetectLanguageUiState = AutoDetectLanguageUiState.Hide,
                currentLanguageUiState = CurrentLanguageUiState.Loading,
                languagesUiState = LanguagesUiState.Loading,
                onBackClick = { },
                onLanguageSelected = { }
            )
        }

        composeTestRule
            .onNodeWithText(getString(R.string.feature_languages_original_title))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun whenIsOriginalLanguage_autoDetectLanguageIsDisplay() {
        composeTestRule.setContent {
            LanguagesScreen(
                isOriginalLanguages = true,
                autoDetectLanguageUiState = AutoDetectLanguageUiState.Shown(
                    defaultAutoLanguage
                ),
                currentLanguageUiState = CurrentLanguageUiState.Loading,
                languagesUiState = LanguagesUiState.Loading,
                onBackClick = { },
                onLanguageSelected = { }
            )
        }

        composeTestRule
            .onNodeWithText(getString(R.string.feature_languages_auto_detect_language))
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun whenLanguagesLoaded_isDisplayAndCurrentLanguageIsMarked() {
        composeTestRule.setContent {
            LanguagesScreen(
                isOriginalLanguages = true,
                autoDetectLanguageUiState = AutoDetectLanguageUiState.Shown(
                    defaultAutoLanguage
                ),
                currentLanguageUiState = CurrentLanguageUiState.Success(
                    languagesTestData[0]
                ),
                languagesUiState = LanguagesUiState.Success(
                    recentLanguagesUsed = languagesTestData.take(1),
                    allLanguages = languagesTestData
                ),
                onBackClick = { },
                onLanguageSelected = { }
            )
        }

        composeTestRule
            .onAllNodes(hasText(languagesTestData[0].languageText))
            .assertCountEquals(2)

        composeTestRule
            .onNodeWithText(languagesTestData[1].languageText)
            .assertExists()
            .assertHasClickAction()

        composeTestRule
            .onAllNodes(
                hasTestTag("languages:checked"),
                useUnmergedTree = true

            )
            .assertCountEquals(2)

        composeTestRule
            .onAllNodes(
                hasTestTag("languages:checked"),
                useUnmergedTree = true
            )
            .assertCountEquals(2)
    }
}