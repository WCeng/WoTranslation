package com.wceng.wotranslation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.core.ui.LocalNetworkOffline
import com.wceng.data.util.NetworkMonitor
import com.wceng.wotranslation.ui.WoApp
import com.wceng.wotranslation.ui.rememberWoAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var isShowSplashScreen = true
        lifecycleScope.launch {
            delay(200)
            isShowSplashScreen = false
        }

        splashScreen.setKeepOnScreenCondition { isShowSplashScreen }

        setContent {
            val woAppState = rememberWoAppState(
                networkMonitor = networkMonitor
            )

            val isOffline by woAppState.isOffline.collectAsStateWithLifecycle()

            CompositionLocalProvider(
                LocalNetworkOffline provides isOffline
            ) {
                WoTheme {
                    WoApp(woAppState = woAppState)
                }
            }
        }
    }
}


