package com.wceng.feature.input

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wceng.core.testing.data.defaultUserLanguages
import com.wceng.core.testing.data.translatesTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var originalLanguageTextString: String

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun whenUserLanguageLoading_anyNotDisplay() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Loading,
                inputTextValue = TextFieldValue(),
                translateUiState = TranslateUiState.EmptyInput,
                recentTranslatesUiState = RecentTranslatesUiState.NotShown,
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(0)
    }

    @Test
    fun userLanguage_whenLoaded_originalAndTargetPlaceHolderIsDisplay() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Success(defaultUserLanguages),
                inputTextValue = TextFieldValue(),
                translateUiState = TranslateUiState.EmptyInput,
                recentTranslatesUiState = RecentTranslatesUiState.NotShown,
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText(
                text = composeTestRule.activity.getString(
                    R.string.feature_input_original_place_holder,
                    defaultUserLanguages.originalLanguage.languageText
                )
            )
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                text = composeTestRule.activity.getString(
                    R.string.feature_input_target_place_holder,
                    defaultUserLanguages.targetLanguage.languageText
                )
            )
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun originalTextField_isFocusedByDefault() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Success(defaultUserLanguages),
                inputTextValue = TextFieldValue(),
                translateUiState = TranslateUiState.EmptyInput,
                recentTranslatesUiState = RecentTranslatesUiState.NotShown,
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText(
                text = defaultUserLanguages.originalLanguage.languageText,
                substring = true
            )
            .assertExists()
            .assertIsFocused()
    }

    @Test
    fun targetTextField_whenTranslateLoading_isShowLoadingAndTrailingIcon() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Success(defaultUserLanguages),
                inputTextValue = TextFieldValue(text = translatesTestData[0].originalText),
                translateUiState = TranslateUiState.Loading,
                recentTranslatesUiState = RecentTranslatesUiState.NotShown,
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText(getString(R.string.feature_input_loading))
            .onParent()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithText(getString(R.string.feature_input_loading))
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(
                "translateResultTrailingIcon",
                useUnmergedTree = true
            )
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun targetTextField_whenTranslateSuccess_showTargetText() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Success(defaultUserLanguages),
                inputTextValue = TextFieldValue(text = translatesTestData[0].originalText),
                translateUiState = TranslateUiState.Success(translatesTestData[0]),
                recentTranslatesUiState = RecentTranslatesUiState.NotShown,
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText(translatesTestData[0].targetText)
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun recentTranslates_whenInputTextHasValue_IsNotDisplay() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Success(defaultUserLanguages),
                inputTextValue = TextFieldValue(text = translatesTestData[0].originalText),
                translateUiState = TranslateUiState.EmptyInput,
                recentTranslatesUiState = RecentTranslatesUiState.NotShown,
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithTag("RecentTranslateLazyColumn")
            .assertIsNotDisplayed()
    }

    @Test
    fun recentTranslates_whenInputTextIsEmptyAndLoaded_IsDisplay() {
        composeTestRule.setContent {
            InputScreen(
                userLanguagesUiState = UserLanguagesUiState.Success(defaultUserLanguages),
                inputTextValue = TextFieldValue(),
                translateUiState = TranslateUiState.EmptyInput,
                recentTranslatesUiState = RecentTranslatesUiState.Success(
                    translatesTestData
                ),
                onTranslateText = {},
                onClearInputText = {},
                onTextFieldValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithTag("RecentTranslateLazyColumn")
            .assertIsDisplayed()

        translatesTestData.forEach {
            composeTestRule.onNodeWithText(it.originalText)
                .assertExists()
                .assertHasClickAction()
        }
    }
}