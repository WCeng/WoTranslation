package com.wceng.feature.bookmark

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wceng.core.testing.data.translatesTestData
import com.wceng.model.CollectableTranslate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BookmarkScreenKtTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun circularProgressIndicatorIsDisplayByDefault() {
        composeTestRule.setContent {
            BookmarksContent(
                bookmarkUiState = BookmarkUiState.Loading
            )
        }

        composeTestRule
            .onNodeWithTag("bookmarks:loading")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun translatesSelection_whenLoaded_isDisplay() {

        val data = translatesTestData.map { CollectableTranslate(it, true) }

        composeTestRule.setContent {
            BookmarksContent(
                bookmarkUiState = BookmarkUiState.Success(
                    data = data
                )
            )
        }

        data.forEach {
            composeTestRule
                .onNodeWithText(it.translate.originalText)
                .assertExists()
                .assertHasClickAction()
        }
    }

    @Test
    fun emptyContent_whenNotMatchingData_isDisplay() {
        composeTestRule.setContent {
            BookmarksContent(
                bookmarkUiState = BookmarkUiState.EmptyResult,
            )
        }

        composeTestRule.onNodeWithTag("bookmark:textField")
            .performTextInput("XXX")

        composeTestRule
            .onNodeWithText(getString(R.string.feature_bookmarks_empty_result_description))
            .assertExists()
            .assertIsDisplayed()
    }

}