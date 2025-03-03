@file:OptIn(ExperimentalCoroutinesApi::class)

package com.wceng.feature.languages

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.wceng.core.domain.GetLanguagesUseCase
import com.wceng.core.domain.GetRecentLanguagesUseCase
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.core.testing.data.defaultAutoLanguage
import com.wceng.core.testing.data.defaultChineseLanguage
import com.wceng.core.testing.data.defaultEnglishLanguage
import com.wceng.core.testing.data.languagesTestData
import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.core.testing.util.MainCoroutineRule
import com.wceng.feature.languages.navigation.LanguagesRoute
import com.wceng.model.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LanguagesViewModelTest() {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userDataRepository = TestUserDataRepository()
    private val languageRepository = TestLanguageRepository()

    private val getUserLanguagesUseCase = GetUserLanguagesUseCase(
        userDataRepository = userDataRepository,
        languageRepository = languageRepository
    )

    private val getRecentLanguagesUseCase = GetRecentLanguagesUseCase(
        userDataRepository = userDataRepository,
        languageRepository = languageRepository
    )

    private val getLanguagesUseCase = GetLanguagesUseCase(
        languageRepository = languageRepository
    )

    private lateinit var viewModel: LanguagesViewModel

    @Before
    fun init() {
        viewModel = LanguagesViewModel(
            languageRepository = languageRepository,
            getUserLanguagesUseCase = getUserLanguagesUseCase,
            getRecentLanguagesUseCase = getRecentLanguagesUseCase,
            getLanguagesUseCase = getLanguagesUseCase,
            savedStateHandle = SavedStateHandle(
                route = LanguagesRoute(isOriginalLanguages = true)
            ),
            userDataRepository = userDataRepository
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.languagesUiState.collect() }

        assertEquals(
            LanguagesUiState.Loading,
            viewModel.languagesUiState.value
        )
    }

    @Test
    fun autoDetectLanguageIsShown() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.autoDetectLanguage.collect() }

        assertEquals(
            AutoDetectLanguageUiState.Shown(defaultAutoLanguage),
            viewModel.autoDetectLanguage.value
        )

    }

    @Test
    fun validateCurrentLanguage() = runTest {
        userDataRepository.setUserData(emptyUserData)

        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.currentLanguage.collect() }

        assertEquals(
            CurrentLanguageUiState.Success(
                defaultEnglishLanguage
            ),
            viewModel.currentLanguage.value
        )
    }

    @Test
    fun languages_whenRecentAndAllLanguagesLoaded_isSuccess() = runTest {
        userDataRepository.setUserData(
            emptyUserData.copy(
                recentOriginalLanguageCodes = listOf("en")
            )
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.languagesUiState.collect() }

        assertEquals(
            LanguagesUiState.Success(
                recentLanguagesUsed = listOf(defaultEnglishLanguage),
                allLanguages = languagesTestData
            ),
            viewModel.languagesUiState.value
        )
    }

    @Test
    fun whenSelectLanguage_userOriginalLanguageIsChanged() = runTest {
        userDataRepository.setUserData(
            emptyUserData.copy(
                recentOriginalLanguageCodes = listOf("en")
            )
        )
        assertEquals(
            defaultEnglishLanguage.languageCode,
            userDataRepository.userData.first().languagePreferences.originalLanguageCode
        )

        viewModel.selectLanguage(
            defaultChineseLanguage
        )

        assertEquals(
            defaultChineseLanguage.languageCode,
            userDataRepository.userData.first().languagePreferences.originalLanguageCode
        )
    }

    @Test
    fun whenSelectLanguage_isRecentLanguagesSavedToUserData() = runTest {
        userDataRepository.setUserData(emptyUserData)

        viewModel.selectLanguage(languagesTestData[0])

        assertEquals(
            listOf(languagesTestData[0].languageCode),
            userDataRepository.userData.first().recentOriginalLanguageCodes
        )
    }

}