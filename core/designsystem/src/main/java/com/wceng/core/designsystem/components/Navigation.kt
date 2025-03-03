package com.wceng.core.designsystem.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WoModelNavigationDrawer(
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            BackHandler(drawerState.isOpen) {
                coroutineScope.launch {
                    drawerState.close()
                }
            }

            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
            ) {
                drawerContent()
            }
        }
    ) {
        content()
    }
}