package com.wceng.feature.languages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.wceng.feature.languages.LanguagesScreen
import kotlinx.serialization.Serializable

@Serializable
data class LanguagesRoute(
    val isOriginalLanguages: Boolean
)

fun NavHostController.navigateToLanguages(
    isOriginalLanguages: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = LanguagesRoute(isOriginalLanguages)) {
        navOptions()
    }
}

fun NavGraphBuilder.languagesScreen(
    onBackClick: () -> Unit
) {
    composable<LanguagesRoute> {
        LanguagesScreen(
            onBackClick = onBackClick
        )
    }
}