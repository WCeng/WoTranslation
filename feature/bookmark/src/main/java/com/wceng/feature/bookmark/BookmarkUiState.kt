package com.wceng.feature.bookmark

import com.wceng.model.CollectableTranslate

sealed interface BookmarkUiState {
    data object Loading : BookmarkUiState

    data object EmptyResult : BookmarkUiState

    data class Success(
        val data: List<CollectableTranslate>
    ) : BookmarkUiState

    data object Error: BookmarkUiState
}