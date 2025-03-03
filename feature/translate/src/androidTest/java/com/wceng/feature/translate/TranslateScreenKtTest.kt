package com.wceng.feature.translate

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wceng.core.testing.data.collectableTranslatesTestData
import com.wceng.core.testing.data.defaultAutoLanguage
import com.wceng.core.testing.data.defaultChineseLanguage
import com.wceng.core.testing.data.defaultEnglishLanguage
import com.wceng.core.testing.data.defaultUserLanguages
import com.wceng.core.testing.data.defaultUserTranslate
import com.wceng.model.UserLanguages
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TranslateScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun topAppBarTitle_whenAllStateIsInitialing_shown() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading
        )

        composeTestRule.onNodeWithText(getString(R.string.feature_translate_top_bar_title))
            .assertExists()
    }

    @Test
    fun userLanguage_whenUserLanguagesLoading_isNotDisplay() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading
        )

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.feature_translate_reverse_language))
            .assertIsNotDisplayed()
    }

    @Test
    fun userLanguage_whenUserLanguagesSuccess_showUserLanguage() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages = defaultUserLanguages
            ),
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading
        )

        composeTestRule
            .onNodeWithText(defaultEnglishLanguage.languageText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(defaultChineseLanguage.languageText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.feature_translate_reverse_language))
            .assertIsEnabled()
    }

    @Test
    fun userLanguageReserveButton_whenOriginalLanguageCodeIsAuto_isDisable() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages = UserLanguages(
                    originalLanguage = defaultAutoLanguage,
                    targetLanguage = defaultChineseLanguage
                )
            ),
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading
        )

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.feature_translate_reverse_language))
            .assertIsNotEnabled()
    }

    @Test
    fun translateFeeds_whenHasData_shown() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages = defaultUserLanguages
            ),
            translateFeedUiState = TranslateFeedUiState.Success(
                translateFeeds = collectableTranslatesTestData
            ),
            userTranslateUiState = UserTranslateUiState.Loading
        )

        collectableTranslatesTestData.onEach {
            composeTestRule.onNodeWithText(it.translate.originalText)
                .assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun originalTextAndLoadingIndicator_whenTranslateLoading_isDisplayed() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Translating(
                originalText = "你好"
            ),
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages = defaultUserLanguages
            ),
            translateFeedUiState = TranslateFeedUiState.Success(
                translateFeeds = collectableTranslatesTestData
            ),
            userTranslateUiState = UserTranslateUiState.Loading
        )

        composeTestRule.onNodeWithText("你好").assertIsDisplayed()

        composeTestRule.onNodeWithTag("translate_loading_indicator").assertIsDisplayed()
    }

    @Test
    fun userTranslate_whenLoadSuccess_translateInfoIsDisplay() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Translating(
                originalText = "你好"
            ),
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages = defaultUserLanguages
            ),
            translateFeedUiState = TranslateFeedUiState.Success(
                translateFeeds = collectableTranslatesTestData
            ),
            userTranslateUiState = UserTranslateUiState.Success(
                userTranslate = defaultUserTranslate
            )
        )

        composeTestRule
            .onAllNodesWithText(defaultUserTranslate.originalLanguageText)
            .assertCountEquals(2)

        composeTestRule
            .onAllNodesWithText(defaultUserTranslate.targetLanguageText)
            .assertCountEquals(2)

        composeTestRule
            .onNodeWithText(defaultUserTranslate.translate.originalText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(defaultUserTranslate.translate.targetText)
            .assertIsDisplayed()
    }

    @Test
    fun userTranslate_whenLoadError_errorInfoIsDisplay() {
        setContent(
            translateScreenUiState = TranslateScreenUiState.Translating(
                originalText = "你好"
            ),
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages = defaultUserLanguages
            ),
            translateFeedUiState = TranslateFeedUiState.Success(
                translateFeeds = collectableTranslatesTestData
            ),
            userTranslateUiState = UserTranslateUiState.Error
        )

        composeTestRule
            .onNodeWithText(getString(R.string.feature_translate_retry))
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    private fun setContent(
        translateScreenUiState: TranslateScreenUiState,
        userLanguagesUiState: UserLanguagesUiState,
        translateFeedUiState: TranslateFeedUiState,
        userTranslateUiState: UserTranslateUiState
    ) {
        composeTestRule.setContent {
            TranslateContent(
                translateScreenUiState = translateScreenUiState,
                userLanguagesUiState = userLanguagesUiState,
                translateFeedUiState = translateFeedUiState,
                userTranslateUiState = userTranslateUiState,
                onClickMenu = { },
                onOriginalLanguageClick = { },
                onTargetLanguageClick = { },
                onReverseLanguage = { },
                onInputCardClick = { },
                onTranslateClick = { },
                onToggleCollect = { _, _ -> },
                onTranslateRetry = { },
                onReadLanguageTextAloud = { },
                onOriginalTextClickWithIndex = { _, _ -> },
                onCloseTranslateResult = { },
                onShowSnackbar = {  }
            )
        }
    }
}