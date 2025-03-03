package com.wceng.core.ui

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wceng.core.designsystem.components.WoIconToggleButton

@Composable
fun CollectToggleIconButton(
    modifier: Modifier = Modifier,
    collected: Boolean,
    onToggleAction: () -> Unit = {}
) {
    WoIconToggleButton(
        checked = collected,
        onCheckedChange = { onToggleAction() },
        icon = {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = stringResource(R.string.core_ui_discard),
            )
        },
        checkedIcon = {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(R.string.core_ui_collect)
            )
        },
        modifier = modifier
    )
}