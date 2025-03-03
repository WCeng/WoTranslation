package com.wceng.feature.translate

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.wceng.core.domain.GetCollectableTranslatesUseCase
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.core.domain.GetUserTranslateUseCase
import com.wceng.core.testing.data.defaultChineseLanguage
import com.wceng.core.testing.data.defaultEnglishLanguage
import com.wceng.core.testing.data.translatesTestData
import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.core.testing.util.MainCoroutineRule
import com.wceng.feature.translate.navigation.TranslateRoute
import com.wceng.model.CollectableTranslate
import com.wceng.model.Language
import com.wceng.model.LanguagePreferences
import com.wceng.model.Translate
import com.wceng.model.UserLanguages
import com.wceng.model.UserTranslate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TranslateViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TranslateViewModel
    private val userDataRepository = TestUserDataRepository()
    private val languageRepository = TestLanguageRepository()
    private val translateRepository = TestTranslateRepository()

    private val getUserLanguagesUseCase =
        GetUserLanguagesUseCase(userDataRepository, languageRepository)

    private val getCollectableTranslatesUseCase = GetCollectableTranslatesUseCase(
        userDataRepository, translateRepository
    )

    private val getUserTranslateUseCase = GetUserTranslateUseCase(
        getUserLanguagesUseCase, translateRepository, userDataRepository,
        languageRepository
    )

    private val originalText = "Hello"

    @Before
    fun setUp() {
        viewModel = TranslateViewModel(
            getCollectableTranslatesUseCase = getCollectableTranslatesUseCase,
            getUserLanguagesUseCase = getUserLanguagesUseCase,
            getUserTranslateUseCase = getUserTranslateUseCase,
            userDataRepository = userDataRepository,
            translateRepository = translateRepository,
            savedStateHandle = SavedStateHandle(
                route = TranslateRoute(
                    originalText = originalText,
                    translateId = null
                ),
            )
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(TranslateScreenUiState.Initialing, viewModel.translateScreenUiState.value)
        assertEquals(UserLanguagesUiState.Loading, viewModel.userLanguagesUiState.value)
        assertEquals(TranslateFeedUiState.Loading, viewModel.translateFeedUiState.value)
        assertEquals(UserTranslateUiState.Loading, viewModel.userTranslateUiState.value)
    }

    @Test
    fun translateScreen_whenOriginalTextWithThreeSpace_isInitialing() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }

        viewModel.translateText("   ")

        assertEquals(
            TranslateScreenUiState.Initialing,
            viewModel.translateScreenUiState.value
        )
    }

    @Test
    fun translateScreenUiState_translateText_isTranslating() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }

        //When
        viewModel.translateText("nihao")

        //Then
        assertEquals(
            TranslateScreenUiState.Translating(originalText = "nihao"),
            viewModel.translateScreenUiState.value
        )
    }

    @Test
    fun userLanguagesUiState_IsSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userLanguagesUiState.collect() }

        assertEquals(UserLanguagesUiState.Loading, viewModel.userLanguagesUiState.value)

        userDataRepository.setUserData(emptyUserData)

        assertEquals(
            UserLanguagesUiState.Success(
                UserLanguages(
                    originalLanguage = defaultEnglishLanguage,
                    targetLanguage = defaultChineseLanguage
                )
            ),
            viewModel.userLanguagesUiState.value
        )
    }

    @Test
    fun translateFeedsUiState_IsSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }

        assertEquals(TranslateFeedUiState.Loading, viewModel.translateFeedUiState.value)

        userDataRepository.setUserData(
            emptyUserData.copy(collectedTranslates = setOf("1", "3"))
        )
        translateRepository.sendTranslates(translatesTestData)

        assertEquals(
            TranslateFeedUiState.Success(
                listOf(
                    CollectableTranslate(
                        translate = translatesTestData[2],
                        collected = true
                    ),
                    CollectableTranslate(
                        translate = translatesTestData[1],
                        collected = false
                    ),
                    CollectableTranslate(
                        translate = translatesTestData[0],
                        collected = true
                    ),
                )
            ),
            viewModel.translateFeedUiState.value
        )
    }

    @Test
    fun userTranslateUiState_translateText_isSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }

        assertEquals(UserTranslateUiState.Loading, viewModel.userTranslateUiState.value)

        translateRepository.sendTranslates(translatesTestData.take(1))
        userDataRepository.setUserData(emptyUserData)

        viewModel.translateText("Hello")

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    translate = translatesTestData[0],
                    originalLanguageText = defaultEnglishLanguage.languageText,
                    targetLanguageText = defaultChineseLanguage.languageText,
                    collected = false
                )
            ), viewModel.userTranslateUiState.value
        )

        userDataRepository.setUserData(emptyUserData.copy(collectedTranslates = setOf("1")))

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    translate = translatesTestData[0],
                    originalLanguageText = defaultEnglishLanguage.languageText,
                    targetLanguageText = defaultChineseLanguage.languageText,
                    collected = true
                )
            ), viewModel.userTranslateUiState.value
        )
    }

    @Test
    fun userLanguages_reserveLanguageWithInitialing_userLanguagesIsToggled() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userLanguagesUiState.collect() }

        userDataRepository.setUserData(emptyUserData)

        assertEquals(
            UserLanguagesUiState.Success(
                UserLanguages(
                    originalLanguage = defaultEnglishLanguage,
                    targetLanguage = defaultChineseLanguage
                )
            ),
            viewModel.userLanguagesUiState.value
        )

        viewModel.reverseLanguage()

        assertEquals(
            UserLanguagesUiState.Success(
                UserLanguages(
                    originalLanguage = defaultChineseLanguage,
                    targetLanguage = defaultEnglishLanguage
                )
            ),
            viewModel.userLanguagesUiState.value
        )
    }

    @Test
    fun userLanguages_reserveLanguageWithTranslating_userLanguagesIsToggled() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userLanguagesUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }

        val firstItem = translatesTestData[0]
        val secondItem = firstItem.run {
            copy(
                id = "newId",
                originalLanguageCode = targetLanguageCode,
                targetLanguageCode = originalLanguageCode,
            )
        }
        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData + listOf(secondItem))
        viewModel.translateText(firstItem.originalText)
        assert(viewModel.userTranslateUiState.value is UserTranslateUiState.Success)

        viewModel.reverseLanguage()

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    translate = secondItem,
                    originalLanguageText = languageRepository.getLanguageTextByCode(secondItem.originalLanguageCode),
                    targetLanguageText = languageRepository.getLanguageTextByCode(secondItem.targetLanguageCode),
                    collected = false
                )
            ),
            viewModel.userTranslateUiState.value
        )
    }

    @Test
    fun userTranslate_showTranslateItem_isTranslatingAndItemShown() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userLanguagesUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        val item = translatesTestData[1]

        viewModel.showTranslate(item)

        assertEquals(
            TranslateScreenUiState.Translating(originalText = item.originalText),
            viewModel.translateScreenUiState.value
        )

        assertEquals(
            UserLanguagesUiState.Success(
                UserLanguages(
                    originalLanguage = Language(
                        languageText = languageRepository.getLanguageTextByCode(item.originalLanguageCode),
                        languageCode = item.originalLanguageCode
                    ),
                    targetLanguage = Language(
                        languageText = languageRepository.getLanguageTextByCode(item.targetLanguageCode),
                        languageCode = item.targetLanguageCode
                    )
                )
            ),
            viewModel.userLanguagesUiState.value
        )

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    item,
                    originalLanguageText = languageRepository.getLanguageTextByCode(item.originalLanguageCode),
                    targetLanguageText = languageRepository.getLanguageTextByCode(item.targetLanguageCode),
                    collected = false
                )
            ),
            viewModel.userTranslateUiState.value
        )

    }

    @Test
    fun translateFeeds_whenCollectOne_OneIsCollected() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        val item = translatesTestData[1]
        viewModel.collectTranslate(item.id, true)

        assertEquals(
            TranslateFeedUiState.Success(
                listOf(
                    CollectableTranslate(
                        translate = translatesTestData[2],
                        collected = false
                    ),
                    CollectableTranslate(
                        translate = translatesTestData[1],
                        collected = true
                    ),
                    CollectableTranslate(
                        translate = translatesTestData[0],
                        collected = false
                    ),
                )
            ),
            viewModel.translateFeedUiState.value
        )
    }

    @Test
    fun userTranslate_whenCollectToggle_userTranslateCollectedIsToggled() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }

        val item = translatesTestData[0]
        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)
        viewModel.translateText(item.originalText)
        assert(viewModel.userTranslateUiState.value is UserTranslateUiState.Success)

        viewModel.collectTranslate(item.id, true)

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    translate = item,
                    originalLanguageText = languageRepository.getLanguageTextByCode(item.originalLanguageCode),
                    targetLanguageText = languageRepository.getLanguageTextByCode(item.targetLanguageCode),
                    collected = true
                )
            ),
            viewModel.userTranslateUiState.value
        )
    }

    @Test
    fun translateScreenUiState_whenCloseUserTranslate_isInitialing() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }

        viewModel.translateText("niha")

        assertEquals(
            TranslateScreenUiState.Translating("niha"),
            viewModel.translateScreenUiState.value
        )

        viewModel.closeTranslateResult()

        assertEquals(
            TranslateScreenUiState.Initialing,
            viewModel.translateScreenUiState.value
        )
    }

    @Test(timeout = 5 * 60 * 1000)
    fun userTranslate_loadError_whenRetryTranslate_isSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(emptyList())

        val item = translatesTestData[2]
        viewModel.translateText(item.originalText)

        assertEquals(
            UserTranslateUiState.Error,
            viewModel.userTranslateUiState.value
        )

        translateRepository.sendTranslates(translatesTestData)

        viewModel.retryTranslate()

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    translate = item,
                    originalLanguageText = languageRepository.getLanguageTextByCode(item.originalLanguageCode),
                    targetLanguageText = languageRepository.getLanguageTextByCode(item.targetLanguageCode),
                    collected = false
                )
            ),
            viewModel.userTranslateUiState.value
        )
    }

//    @Test
//    fun translateFeeds_when_translateText_and_closeTranslateResult_and_changeOriginalLanguage_then_is

    @Test
    fun userTranslate_when_closeUserTranslate_then_isLoading() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }

        translateRepository.sendTranslates(translatesTestData)
        userDataRepository.setUserData(emptyUserData)

        val item = translatesTestData[0]

        viewModel.translateText(item.originalText)

        assertEquals(
            UserTranslateUiState.Success(
                UserTranslate(
                    translate = item,
                    originalLanguageText = defaultEnglishLanguage.languageText,
                    targetLanguageText = defaultChineseLanguage.languageText,
                    collected = false
                )
            ),
            viewModel.userTranslateUiState.value
        )

        viewModel.closeTranslateResult()

        assertEquals(
            UserTranslateUiState.Loading,
            viewModel.userTranslateUiState.value
        )
    }
//    @Test
//    fun userTranslate_whenOriginalLanguageIsAuto_isSuccess() = runTest {
//        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
//        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
//        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
//        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }
//
//        userDataRepository.setUserData(
//            emptyUserData.copy(
//                languagePreferences = LanguagePreferences(
//                    originalLanguageCode = "auto",
//                    targetLanguageCode = "en"
//                )
//            )
//        )
//
//        translateRepository.sendTranslates(translatesTestData)
//
//        val item = translatesTestData[2]
//        viewModel.translateText(item.originalText)
//
//        assertEquals(
//            UserTranslateUiState.Error,
//            viewModel.userTranslateUiState.value
//        )

//        translateRepository.sendTranslates(translatesTestData)
//
//        viewModel.retryTranslate()
//
//        assertEquals(
//            UserTranslateUiState.Success(
//                UserTranslate(
//                    translate = item,
//                    originalLanguageText = languageRepository.getLanguageTextByCode(item.originalLanguageCode),
//                    targetLanguageText = languageRepository.getLanguageTextByCode(item.targetLanguageCode),
//                    collected = false
//                )
//            ),
//            viewModel.userTranslateUiState.value
//        )
//    }
}
