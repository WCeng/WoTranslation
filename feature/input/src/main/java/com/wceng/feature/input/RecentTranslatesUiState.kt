package com.wceng.feature.input

import com.wceng.model.Translate

sealed interface RecentTranslatesUiState {

    data object Loading : RecentTranslatesUiState

    data class Success(
        val translates: List<Translate>
    ) : RecentTranslatesUiState

    data object NotShown: RecentTranslatesUiState
}