package com.wceng.core.testing.util

import com.wceng.data.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestNetworkMonitor() : NetworkMonitor {
    private val isOnlineFlow = MutableStateFlow(true)
    override val isOnline: Flow<Boolean> = isOnlineFlow

    fun setConnected(isConnected: Boolean) {
        isOnlineFlow.value = isConnected
    }
}