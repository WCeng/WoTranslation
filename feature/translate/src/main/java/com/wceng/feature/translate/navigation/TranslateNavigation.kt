package com.wceng.feature.translate.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wceng.feature.translate.ORIGINAL_TEXT_KEY
import com.wceng.feature.translate.TranslateScreen
import kotlinx.serialization.Serializable

@Serializable
data class TranslateRoute(
    val originalText: String? = null,
    val translateId: String? = null
)

@Serializable
data object TranslateBaseRoute

fun NavHostController.navigateToTranslate(
    originalText: String? = null,
    translateId: String? = null,
    navOptions: NavOptions? = null
) {
    navigate(
        route = TranslateRoute(
            originalText = originalText,
            translateId = translateId
        ),
        navOptions = navOptions
    )
}

fun NavHostController.putToTranslateBackStackEntity(originalText: String?) {
    previousBackStackEntry?.savedStateHandle?.set<String?>(
        ORIGINAL_TEXT_KEY, originalText
    )
}

fun NavGraphBuilder.translateSection(
    onMenuClick: () -> Unit,
    onInputClick: (String?, Int?) -> Unit,
    onSelectLanguages: (Boolean) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    inputDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<TranslateBaseRoute>(startDestination = TranslateRoute()) {
        composable<TranslateRoute> { backStackEntity ->
            val value = backStackEntity.savedStateHandle.get<String?>(ORIGINAL_TEXT_KEY)
            TranslateScreen(
                originalText = value,
                onMenuClick = onMenuClick,
                onInputClick = onInputClick,
                onSelectLanguages = onSelectLanguages,
                onShowSnackbar = onShowSnackbar,
                onOriginalTextTranslated = {
                    backStackEntity.savedStateHandle.remove<String?>(ORIGINAL_TEXT_KEY)
                },
            )
        }

        inputDestination()
    }
}