@file:OptIn(ExperimentalCoroutinesApi::class)

package com.wceng.feature.bookmark

import androidx.lifecycle.SavedStateHandle
import com.wceng.core.domain.GetCollectedTranslatesUseCase
import com.wceng.core.testing.data.translatesTestData
import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.core.testing.util.MainCoroutineRule
import com.wceng.model.CollectableTranslate
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

class BookmarkViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val userDataRepository = TestUserDataRepository()
    private val translateRepository = TestTranslateRepository()

    private val getCollectedTranslatesUseCase = GetCollectedTranslatesUseCase(
        userDataRepository = userDataRepository,
        translateRepository = translateRepository
    )

    private lateinit var viewModel: BookmarkViewModel

    @Before
    fun setUp() {
        viewModel = BookmarkViewModel(
            getCollectedTranslatesUseCase = getCollectedTranslatesUseCase,
            userDataRepository = userDataRepository,
            savedStateHandle = SavedStateHandle()
        )

        userDataRepository.setUserData(
            emptyUserData.copy(
                collectedTranslates = setOf(
                    translatesTestData[0].id,
                    translatesTestData[1].id,
                    translatesTestData[2].id
                )
            )
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(
            BookmarkUiState.Loading,
            viewModel.bookmarkUiState.value
        )
    }

    @Test
    fun loadAllCollectedTranslates_whenSearchQueryIsEmpty() = runTest {
        translateRepository.sendTranslates(translatesTestData)
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.bookmarkUiState.collect() }

        viewModel.changeSearchQuery("")

        assertEquals(
            BookmarkUiState.Success(
                translatesTestData.map {
                    CollectableTranslate(
                        translate = it,
                        collected = true
                    )
                }
            ),
            viewModel.bookmarkUiState.value
        )
    }

    @Test
    fun searchQueryWithThreeSpaces_isReturnAllData() = runTest {
        translateRepository.sendTranslates(translatesTestData)
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.bookmarkUiState.collect() }

        viewModel.changeSearchQuery("   ")

        assertEquals(
            BookmarkUiState.Success(
                translatesTestData.map {
                    CollectableTranslate(
                        translate = it,
                        collected = true
                    )
                }
            ),
            viewModel.bookmarkUiState.value
        )
    }

    @Test
    fun emptyResultIsReturned_withNotMatchingQuery() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.bookmarkUiState.collect() }

        translateRepository.sendTranslates(translatesTestData)

        viewModel.changeSearchQuery("XXX")

        assertEquals(
            BookmarkUiState.EmptyResult,
            viewModel.bookmarkUiState.value
        )
    }

    @Test
    fun whenHasMatchingDataWithQuery_stateIsSuccess() = runTest {
        translateRepository.sendTranslates(translatesTestData)

        val item = translatesTestData[1]
        viewModel.changeSearchQuery(item.originalText)

        assertEquals(
            BookmarkUiState.Success(
                listOf(CollectableTranslate(item, true))
            ),
            viewModel.bookmarkUiState.first()
        )
    }

    @Test
    fun collectableTranslate_whenCollectToggle_isCollected() = runTest {
        userDataRepository.setUserData(emptyUserData.copy(collectedTranslates = setOf("1", "3")))
        translateRepository.sendTranslates(translatesTestData)

        val item = translatesTestData[1]

        viewModel.collectTranslate(item.id, true)

        assertEquals(
            BookmarkUiState.Success(
                translatesTestData.map {
                    CollectableTranslate(it, true)
                }
            ),
            viewModel.bookmarkUiState.first()
        )
    }
}