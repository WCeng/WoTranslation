package com.wceng.feature.input

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.wceng.core.domain.GetTranslateNoCached
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.core.testing.data.defaultUserLanguages
import com.wceng.core.testing.data.translatesTestData
import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.core.testing.util.MainCoroutineRule
import com.wceng.feature.input.navigation.InputRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class InputViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    private val userDataRepository = TestUserDataRepository()
    private val translateRepository = TestTranslateRepository()
    private val languageRepository = TestLanguageRepository()

    private val originalText = "Hello"
    private val selectionIndex = 2

    private lateinit var viewModel: InputViewModel

    private fun TestScope.collectInputTextValue() {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.inputTextValue.collect() }
    }

    private fun TestScope.collectRecentTranslate() {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.recentTranslatesUiState.collect() }
    }

    private fun TestScope.collectUserLanguage() {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.userLanguagesUiState.collect() }
    }

    private fun TestScope.collectTranslate() {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.translateUiState.collect() }
    }

    @Before
    fun before() {
        viewModel = InputViewModel(
            savedStateHandle = SavedStateHandle(
                route = InputRoute(inputText = originalText, selectionIndex = selectionIndex)
            ),

            getUserLanguagesUseCase = GetUserLanguagesUseCase(
                userDataRepository,
                languageRepository
            ),
            translateRepository = translateRepository,
            getTranslateNoCached = GetTranslateNoCached(userDataRepository, translateRepository)
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(TextFieldValue(), viewModel.inputTextValue.value)
        assertEquals(UserLanguagesUiState.Loading, viewModel.userLanguagesUiState.value)
        assertEquals(TranslateUiState.EmptyInput, viewModel.translateUiState.value)
        assertEquals(RecentTranslatesUiState.Loading, viewModel.recentTranslatesUiState.value)
    }

    @Test
    fun inputTextValue_whenHasValueInSavedStateHandle_thenIsCorrect() = runTest {
        collectInputTextValue()
        assertEquals(
            TextFieldValue(text = originalText, selection = TextRange(selectionIndex)),
            viewModel.inputTextValue.value
        )
    }

    @Test
    fun userLanguage_whenSuccessLoaded_isExpected() = runTest {
        userDataRepository.setUserData(emptyUserData)
        collectUserLanguage()

        assertEquals(
            UserLanguagesUiState.Success(defaultUserLanguages),
            viewModel.userLanguagesUiState.value
        )
    }

    @Test
    fun translate_whenInputTextHasValue_thenIsSuccess() = runTest {
        collectInputTextValue()
        collectUserLanguage()
        collectTranslate()
        collectRecentTranslate()

        userDataRepository.setUserData(emptyUserData)

        advanceUntilIdle()

        assertTrue(
            viewModel.translateUiState.value is TranslateUiState.Success
        )
    }

    @Test
    fun translate_whenInputTextIsNull_thenIsEmptyInput() = runTest {
        collectInputTextValue()
        collectUserLanguage()
        collectTranslate()

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        viewModel.clearInputText()

        assertEquals(
            TranslateUiState.EmptyInput,
            viewModel.translateUiState.value
        )
    }

    @Test
    fun recentTranslate_whenInputTextHasValue_isNotShown() = runTest {
        collectInputTextValue()
        collectUserLanguage()
        collectRecentTranslate()

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        assertEquals(
            RecentTranslatesUiState.NotShown,
            viewModel.recentTranslatesUiState.value
        )
    }

    @Test
    fun recentTranslate_whenInputTextIsNull_isSuccess() = runTest {
        collectInputTextValue()
        collectUserLanguage()
        collectRecentTranslate()

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        viewModel.clearInputText()

        assertEquals(
            RecentTranslatesUiState.Success(
                translates = translatesTestData.reversed()
            ),
            viewModel.recentTranslatesUiState.value
        )
    }

    @Test
    fun inputTextValue_whenChangeNewValue_isExcepted() = runTest {
        collectInputTextValue()
        collectUserLanguage()
        collectTranslate()

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        viewModel.changeInputTextValue(TextFieldValue(text = "What", selection = TextRange(3)))

        assertEquals(
            TextFieldValue(text = "What", selection = TextRange(3)),
            viewModel.inputTextValue.value
        )

        advanceUntilIdle()
    }


}