package com.wceng.feature.languages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.wceng.common.result.Result
import com.wceng.common.result.asResult
import com.wceng.core.domain.GetLanguagesUseCase
import com.wceng.core.domain.GetRecentLanguagesUseCase
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.core.domain.LanguageSortField
import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.feature.languages.navigation.LanguagesRoute
import com.wceng.model.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    languageRepository: LanguageRepository,
    getUserLanguagesUseCase: GetUserLanguagesUseCase,
    getRecentLanguagesUseCase: GetRecentLanguagesUseCase,
    getLanguagesUseCase: GetLanguagesUseCase,
    private val userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val isOriginalLanguages = savedStateHandle.toRoute<LanguagesRoute>().isOriginalLanguages

    val autoDetectLanguage: StateFlow<AutoDetectLanguageUiState> =
        autoDetectLanguage(isOriginalLanguages, languageRepository)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                AutoDetectLanguageUiState.Hide
            )

    val currentLanguage: StateFlow<CurrentLanguageUiState> = getUserLanguagesUseCase()
        .map {
            CurrentLanguageUiState.Success(
                current = if (isOriginalLanguages)
                    it.originalLanguage
                else
                    it.targetLanguage
            )

        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            CurrentLanguageUiState.Loading
        )

    val languagesUiState: StateFlow<LanguagesUiState> =
        combine(
            getRecentLanguagesUseCase(isOriginalLanguages),
            getLanguagesUseCase(sortBy = LanguageSortField.NAME),
            ::Pair
        )
            .asResult()
            .map {
                when (it) {
                    Result.Loading -> LanguagesUiState.Loading
                    is Result.Error -> LanguagesUiState.Error
                    is Result.Success -> LanguagesUiState.Success(
                        recentLanguagesUsed = it.data.first,
                        allLanguages = it.data.second
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                LanguagesUiState.Loading
            )

    fun selectLanguage(language: Language) = viewModelScope.launch {
        if (isOriginalLanguages) {
            userDataRepository.setOriginalLanguageCode(language.languageCode)
            userDataRepository.setRecentOriginalLanguageCode(language.languageCode)
        } else {
            userDataRepository.setTargetLanguageCode(language.languageCode)
            userDataRepository.setRecentTargetLanguageCode(language.languageCode)
        }
    }
}

private fun autoDetectLanguage(
    isOriginalLanguages: Boolean,
    languageRepository: LanguageRepository
): Flow<AutoDetectLanguageUiState> =
    flow {
        if (isOriginalLanguages) {
            val autoLanguage = languageRepository.getAutoLanguage()
            emit(AutoDetectLanguageUiState.Shown(autoLanguage))
        } else {
            emit(AutoDetectLanguageUiState.Hide)
        }
    }

sealed interface LanguagesUiState {
    data object Loading : LanguagesUiState

    data class Success(
        val recentLanguagesUsed: List<Language>,
        val allLanguages: List<Language>
    ) : LanguagesUiState

    data object Error : LanguagesUiState
}

sealed interface AutoDetectLanguageUiState {
    data class Shown(
        val language: Language
    ) : AutoDetectLanguageUiState

    data object Hide : AutoDetectLanguageUiState
}

sealed interface CurrentLanguageUiState {
    data object Loading : CurrentLanguageUiState

    data class Success(
        val current: Language
    ) : CurrentLanguageUiState
}