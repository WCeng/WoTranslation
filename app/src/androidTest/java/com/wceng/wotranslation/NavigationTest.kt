package com.wceng.wotranslation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import com.wceng.feature.bookmark.R as bookmarkR
import com.wceng.feature.languages.R as langR
import com.wceng.feature.translate.R as translateR

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var translateRepository: TranslateRepository

    @Inject
    lateinit var languageRepository: LanguageRepository

    @Inject
    lateinit var userDataRepository: UserDataRepository

    private val appName by composeTestRule.stringResource(R.string.app_name)
    private val woTranslate by composeTestRule.stringResource(translateR.string.feature_translate_top_bar_title)
    private val simpleTranslateOriginalText = "Hello"
    private val simpleTranslateTargetText = "你好"
    private val language = "英语"
    private val bookmark by composeTestRule.stringResource(R.string.app_book_mark)
    private val home by composeTestRule.stringResource(R.string.app_main_page)
    private val bookmarkSearch by composeTestRule.stringResource(bookmarkR.string.feature_bookmarks_search_place_holder)
    private val languageBackDesc by composeTestRule.stringResource(langR.string.feature_languages_back)
    private val inputTextPlaceHolder by composeTestRule.stringResource(translateR.string.feature_translate_text_field_place_holder)

    @Before
    fun init() = hiltAndroidRule.inject()

    @Test
    fun firstScreen_isTranslate() {
        composeTestRule.onNodeWithText(woTranslate)
            .assertIsDisplayed()
    }

    @Test
    fun drawerSheet_navigateToOtherDestination_isClose(){
        composeTestRule.apply {
            openDrawer()
            onNodeWithText(bookmark).performClick()
            onNodeWithText(home).assertIsNotDisplayed()
        }
    }

    @Test
    fun navigationBar_navigateToPreviouslySelectedTab_restoreContent() {
        composeTestRule.onNodeWithText(simpleTranslateOriginalText).performClick()

        openDrawer()
        composeTestRule.onNodeWithText(bookmark).performClick()

        //back to home
        openDrawer()
        composeTestRule.onNodeWithText(home).performClick()

        composeTestRule.onNodeWithText(simpleTranslateTargetText).assertIsDisplayed()
    }

    @Test
    fun navigationBar_reselectTab_keepStates() {
        composeTestRule.onNodeWithText(simpleTranslateOriginalText).performClick()

        openDrawer()
        composeTestRule.onNodeWithText(home).performClick()

        composeTestRule.onNodeWithText(simpleTranslateTargetText).assertIsDisplayed()
    }

    @Test
    fun bookmarkScreen_clickBackButton_isTranslateScreen() {
        composeTestRule.apply {
            openDrawer()
            onNodeWithText(bookmark).performClick()
            Espresso.pressBack()
            onNodeWithText(woTranslate).assertIsDisplayed()
        }
    }

    @Test
    fun languageScreen_iconButtonBackOrSystemBack_backToTranslateScreen() {
        composeTestRule.apply {
            onNodeWithText(language).performClick()
            onNodeWithContentDescription(languageBackDesc).performClick()
            onNodeWithText(woTranslate).assertIsDisplayed()

            onNodeWithText(language).performClick()
            Espresso.pressBack()
            onNodeWithText(woTranslate).assertIsDisplayed()
        }
    }

    @Test(expected = NoActivityResumedException::class)
    fun inputScreen_clickBack_backToTranslateScreen() {
        composeTestRule.apply {
            onNodeWithText(inputTextPlaceHolder).performClick()
            Espresso.closeSoftKeyboard()
            Espresso.pressBack()
            onNodeWithText(woTranslate).assertIsDisplayed()
            Espresso.pressBack()
            //then app quits
        }
    }

    @Test
    fun bookmark_whenClickTranslate_intoTranslateScreen_clickBack_backToBookmark() = runTest {
        val translate = translateRepository.getTranslates().first().first()
        userDataRepository.setCollectTranslate(
            translate.id, true
        )

        composeTestRule.apply {
            openDrawer()
            onNodeWithText(bookmark).performClick()
            onNodeWithText(translate.originalText).performClick()

            onNodeWithText(woTranslate).assertIsDisplayed()

            Espresso.pressBack()
            onNodeWithText(translate.originalText).assertIsDisplayed()
        }
    }

    private fun openDrawer() {
        composeTestRule.onRoot()
            .performTouchInput {
                swipeRight()
            }
    }
}

