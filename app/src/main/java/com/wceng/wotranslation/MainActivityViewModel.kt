package com.wceng.wotranslation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.DarkThemeConfig
import com.wceng.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData
        .map(MainActivityUiState::Success)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            MainActivityUiState.Loading
        )
}

internal sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(
        private val userData: UserData
    ) : MainActivityUiState {
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean {
            return when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }
        }
    }

    fun shouldKeepSplashScreen() = this is Loading

    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}

