package com.wceng.wotranslation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.wceng.feature.bookmark.navigation.bookmarkScreen
import com.wceng.feature.input.navigation.inputScreen
import com.wceng.feature.input.navigation.navigateToInputInput
import com.wceng.feature.languages.navigation.languagesScreen
import com.wceng.feature.languages.navigation.navigateToLanguages
import com.wceng.feature.translate.navigation.TranslateBaseRoute
import com.wceng.feature.translate.navigation.navigateToTranslate
import com.wceng.feature.translate.navigation.putToTranslateBackStackEntity
import com.wceng.feature.translate.navigation.translateSection
import com.wceng.wotranslation.ui.WoAppState

@Composable
fun WoNavHost(
    modifier: Modifier = Modifier,
    appState: WoAppState,
    onMenuClick: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
) {
    val navController = appState.navHostController

    NavHost(
        navController = navController,
        startDestination = TranslateBaseRoute,
        modifier = modifier
    ) {
        translateSection(
            onMenuClick = onMenuClick,
            onInputClick = navController::navigateToInputInput,
            onSelectLanguages = navController::navigateToLanguages,
            onShowSnackbar = onShowSnackbar
        ) {
            inputScreen(
                onTranslateText = { originalText ->
                    navController.putToTranslateBackStackEntity(originalText)
                    navController.popBackStack()
                }
            )

            languagesScreen(onBackClick = navController::popBackStack)
        }

        bookmarkScreen(
            onTranslateClick = {
                navController.navigateToTranslate(translateId = it)
            },
            onMenuClick = onMenuClick
        )
    }
}