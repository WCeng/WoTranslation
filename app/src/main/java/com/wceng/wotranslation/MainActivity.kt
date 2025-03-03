package com.wceng.wotranslation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.core.ui.LocalNetworkOffline
import com.wceng.data.util.NetworkMonitor
import com.wceng.wotranslation.ui.WoApp
import com.wceng.wotranslation.ui.rememberWoAppState
import dagger.hilt.android.AndroidEntryPoint
import java.security.KeyStore
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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


