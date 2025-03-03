package com.wceng.feature.input

import com.wceng.model.UserLanguages

sealed interface UserLanguagesUiState {

    data object Loading : UserLanguagesUiState

    data class Success(
        val userLanguages: UserLanguages
    ) : UserLanguagesUiState
}