package com.wceng.feature.translate

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.wceng.common.result.Result
import com.wceng.common.result.asResult
import com.wceng.core.domain.GetCollectableTranslatesUseCase
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.core.domain.GetUserTranslateUseCase
import com.wceng.core.network.BuildConfig
import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.feature.translate.navigation.TranslateRoute
import com.wceng.model.AUTO_LANGUAGE_CODE
import com.wceng.model.CollectableTranslate
import com.wceng.model.LanguagePreferences
import com.wceng.model.Translate
import com.wceng.model.UserLanguages
import com.wceng.model.UserTranslate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

internal const val ORIGINAL_TEXT_KEY = "original_text_key"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TranslateViewModel @Inject constructor(
    getCollectableTranslatesUseCase: GetCollectableTranslatesUseCase,
    getUserLanguagesUseCase: GetUserLanguagesUseCase,
    getUserTranslateUseCase: GetUserTranslateUseCase,
    private val userDataRepository: UserDataRepository,
    private val savedStateHandle: SavedStateHandle,
    private val translateRepository: TranslateRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<TranslateRoute>()

    private val _originalText: StateFlow<String> =
        savedStateHandle.getStateFlow(key = ORIGINAL_TEXT_KEY, route.originalText ?: "")

    init {
        viewModelScope.launch {
            if (route.translateId != null) {
                showTranslate(translateRepository.getTranslate(route.translateId).first())
            }
        }
    }

    private val _retryTranslateTrigger = MutableStateFlow(false)

    val translateScreenUiState: StateFlow<TranslateScreenUiState> =
        _originalText.map { originalText ->
            if (originalText.isNotBlank()) TranslateScreenUiState.Translating(originalText)
            else TranslateScreenUiState.Initialing
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TranslateScreenUiState.Initialing // 使用正确的初始状态
        )

    val userLanguagesUiState: StateFlow<UserLanguagesUiState> =
        getUserLanguagesUseCase().asResult().map { result ->
            when (result) {
                Result.Loading -> UserLanguagesUiState.Loading
                is Result.Success -> UserLanguagesUiState.Success(result.data)
                is Result.Error -> UserLanguagesUiState.Error
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UserLanguagesUiState.Loading
        )

    val translateFeedUiState: StateFlow<TranslateFeedUiState> =
        getCollectableTranslatesUseCase().asResult().map {
            when (it) {
                Result.Loading -> TranslateFeedUiState.Loading
                is Result.Error -> TranslateFeedUiState.Error
                is Result.Success -> TranslateFeedUiState.Success(it.data)
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TranslateFeedUiState.Loading
        )

    val userTranslateUiState: StateFlow<UserTranslateUiState> =
        _originalText.combine(_retryTranslateTrigger, ::Pair)
            .distinctUntilChanged()
            .flatMapLatest { (originalText, _) ->
                if (originalText.isBlank()) {
                    flow {
                        emit(UserTranslateUiState.Loading)
                    }
                } else
                    getUserTranslateUseCase(originalText).asResult().map {
                        when (it) {
                            Result.Loading -> UserTranslateUiState.Loading
                            is Result.Error -> {
                                UserTranslateUiState.Error
                            }

                            is Result.Success -> UserTranslateUiState.Success(it.data)
                        }
                    }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = UserTranslateUiState.Loading
            )

//    val onboardingUiState: StateFlow<> =

    fun reverseLanguage() = viewModelScope.launch {
        val languagePreferences = userDataRepository.userData.first().languagePreferences

        userDataRepository.setLanguagePreferences(
            LanguagePreferences(
                languagePreferences.targetLanguageCode,
                languagePreferences.originalLanguageCode
            )
        )
    }

    fun showTranslate(translate: Translate) = viewModelScope.launch {
        userDataRepository.setLanguagePreferences(
            LanguagePreferences(
                originalLanguageCode = translate.originalLanguageCode,
                targetLanguageCode = translate.targetLanguageCode
            )
        )

        savedStateHandle[ORIGINAL_TEXT_KEY] = translate.originalText
    }

    fun collectTranslate(translateId: String, collected: Boolean) = viewModelScope.launch {
        userDataRepository.setCollectTranslate(translateId, collected)
    }

    fun retryTranslate() = viewModelScope.launch {
        _retryTranslateTrigger.value = !_retryTranslateTrigger.value
    }

    fun closeTranslateResult() = viewModelScope.launch {
        savedStateHandle[ORIGINAL_TEXT_KEY] = ""
    }

    fun translateText(text: String) = viewModelScope.launch {
        savedStateHandle[ORIGINAL_TEXT_KEY] = text
    }
}

sealed interface TranslateScreenUiState {
    data object Initialing : TranslateScreenUiState
    data class Translating(val originalText: String) : TranslateScreenUiState
}

sealed interface UserLanguagesUiState {
    data object Loading : UserLanguagesUiState
    data object Error : UserLanguagesUiState

    data class Success(
        val userLanguages: UserLanguages
    ) : UserLanguagesUiState {
        val canReverse = userLanguages.originalLanguage.languageCode != AUTO_LANGUAGE_CODE
    }
}

sealed interface TranslateFeedUiState {
    data object Loading : TranslateFeedUiState
    data object Error : TranslateFeedUiState
    data class Success(val translateFeeds: List<CollectableTranslate>) : TranslateFeedUiState
}

sealed interface UserTranslateUiState {
    data object Loading : UserTranslateUiState
    data object Error : UserTranslateUiState
    data class Success(val userTranslate: UserTranslate) : UserTranslateUiState
}

sealed interface OnboardingUiState {
//    data
}