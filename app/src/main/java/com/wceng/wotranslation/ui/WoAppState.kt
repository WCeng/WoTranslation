package com.wceng.wotranslation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.wceng.data.util.NetworkMonitor
import com.wceng.feature.bookmark.navigation.navigationToBookmark
import com.wceng.feature.translate.navigation.navigateToTranslate
import com.wceng.wotranslation.navigation.TopLevelDestination
import com.wceng.wotranslation.navigation.TopLevelDestination.BOOKMARK
import com.wceng.wotranslation.navigation.TopLevelDestination.TRANSLATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@Composable
fun rememberWoAppState(
    networkMonitor: NetworkMonitor,
    navHostController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): WoAppState {
    return remember {
        WoAppState(
            navHostController = navHostController,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope
        )
    }
}

@Stable
class WoAppState(
    val navHostController: NavHostController,
    val networkMonitor: NetworkMonitor,
    val coroutineScope: CoroutineScope,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navHostController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val topLevelDestinations: List<TopLevelDestination>
        get() = TopLevelDestination.entries

    val isOffline = networkMonitor.isOnline
        .map (Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = false
        )

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TRANSLATE -> navHostController.navigateToTranslate(
                navOptions = navOptions
            )

            BOOKMARK -> navHostController.navigationToBookmark(
                navOptions = navOptions
            )
        }
    }

}