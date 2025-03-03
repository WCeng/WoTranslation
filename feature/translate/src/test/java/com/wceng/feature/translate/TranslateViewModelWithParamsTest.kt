package com.wceng.feature.translate

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.wceng.core.domain.GetCollectableTranslatesUseCase
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.core.domain.GetUserTranslateUseCase
import com.wceng.core.testing.data.translatesTestData
import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.core.testing.util.MainCoroutineRule
import com.wceng.feature.translate.navigation.TranslateRoute
import com.wceng.model.UserTranslate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TranslateViewModelWithParamsTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

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

    private fun viewModel(originalText: String? = null, translateId: String? = null) =
        TranslateViewModel(
            getCollectableTranslatesUseCase = getCollectableTranslatesUseCase,
            getUserLanguagesUseCase = getUserLanguagesUseCase,
            getUserTranslateUseCase = getUserTranslateUseCase,
            userDataRepository = userDataRepository,
            translateRepository = translateRepository,
            savedStateHandle = SavedStateHandle(
                route = TranslateRoute(
                    originalText = originalText,
                    translateId = translateId
                ),
            )
        )

    @Test
    fun userTranslate_whenHasTranslateId_and_originalTextIsNull_then_isSuccess() = runTest {
        val item = translatesTestData[0]
        val viewModel = viewModel(originalText = null, translateId = item.id)

        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }

        translateRepository.sendTranslates(translatesTestData)
        userDataRepository.setUserData(emptyUserData)

        assertEquals(
            UserTranslateUiState.Success(
                userTranslate = UserTranslate(
                    translate = item,
                    originalLanguageText = languageRepository.getLanguageTextByCode(item.originalLanguageCode),
                    targetLanguageText = languageRepository.getLanguageTextByCode(item.targetLanguageCode),
                    collected = false
                )
            ),
            viewModel.userTranslateUiState.value
        )
    }

    @Test
    fun userTranslate_whenHasTranslateId_and_originalTextHasValue_then_isSuccess() = runTest {
        val firstItem = translatesTestData[0]
        val secondItem = translatesTestData[1]

        translateRepository.sendTranslates(translatesTestData)
        userDataRepository.setUserData(emptyUserData)

        val viewModel =
            viewModel(originalText = firstItem.originalText, translateId = secondItem.id)

        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateScreenUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userTranslateUiState.collect() }
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateFeedUiState.collect() }


        assertEquals(
            UserTranslateUiState.Success(
                userTranslate = UserTranslate(
                    translate = secondItem,
                    originalLanguageText = languageRepository.getLanguageTextByCode(secondItem.originalLanguageCode),
                    targetLanguageText = languageRepository.getLanguageTextByCode(secondItem.targetLanguageCode),
                    collected = false
                )
            ),
            viewModel.userTranslateUiState.value
        )
    }

}