package com.wceng.feature.bookmark

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wceng.common.result.Result
import com.wceng.common.result.asResult
import com.wceng.core.domain.GetCollectedTranslatesUseCase
import com.wceng.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_QUERY = "searchQuery"

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getCollectedTranslatesUseCase: GetCollectedTranslatesUseCase,
    private val userDataRepository: UserDataRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(
        key = SEARCH_QUERY, initialValue = ""
    )

    val bookmarkUiState: StateFlow<BookmarkUiState> =
        searchQuery
            .combine(getCollectedTranslatesUseCase()) { searchQuery, collectedTranslates ->
                collectedTranslates
                    .filter {
                        if(searchQuery.isBlank()) true
                        else searchQuery in it.translate.originalText || searchQuery in it.translate.targetText
                    }
            }
            .asResult()
            .map {
                when (it) {
                    Result.Loading -> BookmarkUiState.Loading
                    is Result.Success -> {
                        if (it.data.isEmpty()) {
                            BookmarkUiState.EmptyResult
                        } else {
                            BookmarkUiState.Success(it.data)
                        }
                    }

                    is Result.Error -> BookmarkUiState.Error
                }

            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = BookmarkUiState.Loading
            )


    fun changeSearchQuery(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun collectTranslate(translateId: String, collected: Boolean) = viewModelScope.launch {
        userDataRepository.setCollectTranslate(translateId, collected)
    }
}


