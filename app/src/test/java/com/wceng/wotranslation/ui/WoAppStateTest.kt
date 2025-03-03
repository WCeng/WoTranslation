package com.wceng.wotranslation.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.wceng.wotranslation.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class WoAppStateTest(){

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var state: WoAppState

    @Test
    fun a(){
//        composeTestRule.setContent {
//            val testNavController = rememberTestNavController()
//            state  = remember {
//                WoAppState(
//                    navHostController = testNavController
//                )
//            }
//        }

        assert(true)
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
