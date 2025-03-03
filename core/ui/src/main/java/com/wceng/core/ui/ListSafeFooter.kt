package com.wceng.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier

fun LazyListScope.listSafeFooter() {
    item {
        Spacer(
            Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing)
        )
    }
}