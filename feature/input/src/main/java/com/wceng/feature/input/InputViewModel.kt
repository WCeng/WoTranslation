package com.wceng.feature.input

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.wceng.common.result.Result
import com.wceng.common.result.asResult
import com.wceng.core.domain.GetTranslateNoCached
import com.wceng.core.domain.GetUserLanguagesUseCase
import com.wceng.data.repository.TranslateRepository
import com.wceng.feature.input.navigation.InputRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

const val INPUT_TEXT_KEY = "input_text_key"
const val SELECTION_INDEX_KEY = "selection_index_key"
private const val recentTranslatesMaxCount = 10

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class InputViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getUserLanguagesUseCase: GetUserLanguagesUseCase,
    translateRepository: TranslateRepository,
    getTranslateNoCached: GetTranslateNoCached
) : ViewModel() {

    private val inputRoute = savedStateHandle.toRoute<InputRoute>()

    private val _inputText =
        savedStateHandle.getStateFlow(INPUT_TEXT_KEY, initialValue = inputRoute.inputText)

    private val _selectionIndex: StateFlow<Int?> =
        savedStateHandle.getStateFlow(
            SELECTION_INDEX_KEY,
            initialValue = inputRoute.selectionIndex
        )

    val inputTextValue: StateFlow<TextFieldValue> =
        combine(_inputText, _selectionIndex) { inputText, selectionIndex ->
            TextFieldValue(text = inputText ?: "",
                selection = selectionIndex?.let { TextRange(it) } ?: TextRange.Zero)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TextFieldValue()
        )

    val recentTranslatesUiState: StateFlow<RecentTranslatesUiState> =
        _inputText.flatMapLatest { originalText ->
            if (originalText.isNullOrEmpty()) {
                translateRepository.getLatestTranslates(limit = recentTranslatesMaxCount)
                    .map { RecentTranslatesUiState.Success(it) }
            } else {
                flow<RecentTranslatesUiState> {
                    emit(RecentTranslatesUiState.NotShown)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RecentTranslatesUiState.Loading
        )

    val userLanguagesUiState: StateFlow<UserLanguagesUiState> =
        getUserLanguagesUseCase().map(UserLanguagesUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = UserLanguagesUiState.Loading
            )

    val translateUiState: StateFlow<TranslateUiState> =
        _inputText
            .map { it?.trim() }
            .distinctUntilChanged()
            .debounce(1000)
            .flatMapLatest { originalText ->
                if (originalText.isNullOrEmpty()) {
                    flowOf(TranslateUiState.EmptyInput)
                } else {
                    getTranslateNoCached(originalText)
                        .asResult()
                        .map {
                            when (it) {
                                Result.Loading -> TranslateUiState.Loading
                                is Result.Success -> TranslateUiState.Success(it.data)
                                is Result.Error -> TranslateUiState.Error
                            }
                        }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TranslateUiState.EmptyInput
            )

    fun clearInputText() {
        savedStateHandle[INPUT_TEXT_KEY] = null
    }

    fun changeInputTextValue(textFieldValue: TextFieldValue) {
        savedStateHandle[INPUT_TEXT_KEY] = textFieldValue.text
        savedStateHandle[SELECTION_INDEX_KEY] = textFieldValue.selection.start
    }
}
