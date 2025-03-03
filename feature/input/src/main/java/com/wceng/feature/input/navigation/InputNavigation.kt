package com.wceng.feature.input.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.wceng.feature.input.InputScreen
import kotlinx.serialization.Serializable

@Serializable
data class InputRoute(
    val inputText: String? = null,
    val selectionIndex: Int? = null
)

fun NavHostController.navigateToInputInput(
    inputText: String? = null,
    selectionIndex: Int? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        route = InputRoute(inputText = inputText, selectionIndex = selectionIndex),
    ) {
        navOptions()
    }
}

fun NavGraphBuilder.inputScreen(
    onTranslateText: (String) -> Unit,
) {
    composable<InputRoute> {
        InputScreen(
            onTranslateText = onTranslateText
        )
    }
}
