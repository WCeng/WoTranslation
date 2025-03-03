package com.wceng.feature.input

import com.wceng.model.Translate

sealed interface TranslateUiState {

    data object EmptyInput : TranslateUiState

    data object Loading: TranslateUiState

    data class Success(
        val translate: Translate
    ): TranslateUiState

    data object Error: TranslateUiState
}