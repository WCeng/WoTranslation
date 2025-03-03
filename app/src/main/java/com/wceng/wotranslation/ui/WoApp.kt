package com.wceng.wotranslation.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.wceng.core.designsystem.components.WoModelNavigationDrawer
import com.wceng.core.designsystem.theme.WoBackground
import com.wceng.core.ui.LocalNetworkOffline
import com.wceng.wotranslation.R
import com.wceng.wotranslation.navigation.WoNavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Composable
fun WoApp(
    modifier: Modifier = Modifier,
    woAppState: WoAppState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isOffline = LocalNetworkOffline.current
    val notConnectedMessage = stringResource(R.string.app_not_connected)
    val actionLabel = stringResource(R.string.app_snackbar_action_label)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
                actionLabel = actionLabel
            )
        }
    }
    WoBackground(modifier = modifier) {
        WoApp(
            appState = woAppState,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun WoApp(
    modifier: Modifier = Modifier,
    appState: WoAppState,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    WoModelNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            appState.topLevelDestinations.forEach { destination ->
                val selected =
                    appState.currentDestination.isRouteInHierarchy(destination.baseRoute)

                NavigationDrawerItem(
                    label = { Text(text = stringResource(destination.label)) },
                    selected = selected,
                    onClick = {
                        appState.navigateToTopLevelDestination(destination)
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector =
                            if (selected) destination.checkedIcon else destination.icon,
                            contentDescription = null
                        )
                    }
                )
            }
        }) {
        Scaffold(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = Color.Transparent,
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                )
            },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { padding ->
            WoNavHost(
                modifier = modifier
                    .consumeWindowInsets(padding)
                    .padding(padding),
                appState = appState,
                onMenuClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                },
                onShowSnackbar = {
                    snackbarHostState.showSnackbar(
                        message = it,
                        duration = SnackbarDuration.Short
                    )
                }
            )
        }
    }

}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
