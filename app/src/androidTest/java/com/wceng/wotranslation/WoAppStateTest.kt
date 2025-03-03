@file:OptIn(ExperimentalCoroutinesApi::class)

package com.wceng.wotranslation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wceng.core.testing.util.TestNetworkMonitor
import com.wceng.wotranslation.ui.WoAppState
import com.wceng.wotranslation.ui.rememberWoAppState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WoAppStateTest {

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val networkMonitor = TestNetworkMonitor()

    lateinit var state: WoAppState

    @Test
    fun woAppState_currentDestination() = runTest {
        var currentDestination: String? = null

        composeTestRule.setContent {
            val testNavController = rememberTestNavController()
            state = remember {
                WoAppState(
                    navHostController = testNavController,
                    networkMonitor = networkMonitor,
                    coroutineScope = backgroundScope
                )
            }

            // Update currentDestination whenever it changes
            currentDestination = state.currentDestination?.route

            // Navigate to destination b once
            LaunchedEffect(Unit) {
                testNavController.setCurrentDestination("b")
            }
        }

        Assert.assertEquals("b", currentDestination)
    }

    @Test
    fun woAppState_destinations() {
        composeTestRule.setContent {
            state = rememberWoAppState(
                networkMonitor = networkMonitor,
            )
        }

        assert(state.topLevelDestinations[0].name.contains("translate", ignoreCase = true))
        assert(state.topLevelDestinations[1].name.contains("bookmark", ignoreCase = true))
    }

    @Test
    fun woAppState_whenNetworkIsOffline_stateIsOffline() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
            state = rememberWoAppState(
                networkMonitor = networkMonitor,
                coroutineScope = backgroundScope
            )
        }

        backgroundScope.launch {
            state.isOffline.collect()
        }

        networkMonitor.setConnected(false)

        assert(state.isOffline.value)
    }
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
    val context = LocalContext.current
    return remember {
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "a") {
                composable("a") { }
                composable("b") { }
                composable("c") { }
            }
        }
    }
}
