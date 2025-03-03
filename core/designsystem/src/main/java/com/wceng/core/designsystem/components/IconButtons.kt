package com.wceng.core.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wceng.core.designsystem.ThemePreview
import com.wceng.core.designsystem.theme.WoTheme

@Composable
fun WoIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
) {
    IconToggleButton (
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = if (checked) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = WoIconButtonDefaults.DISABLED_ICON_BUTTON_CONTAINER_ALPHA,
                )
            } else {
                Color.Transparent
            }
        ),
    ) {
        if (checked) checkedIcon() else icon()
    }
}

@ThemePreview
@Composable
private fun WoIconToggleButtonCheckedPreview() {
    WoTheme {
        WoIconToggleButton(
            checked = true,
            onCheckedChange = {},
            icon = {
                Icon(imageVector = Icons.Rounded.StarOutline, contentDescription = "collect")
            },
            checkedIcon = {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "collect")
            }
        )
    }
}

@ThemePreview
@Composable
private fun WoIconToggleButtonUnCheckedPreview() {
    WoTheme {
        WoIconToggleButton(
            checked = false,
            onCheckedChange = {},
            icon = {
                Icon(imageVector = Icons.Rounded.StarOutline, contentDescription = "collect")
            },
            checkedIcon = {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "collect")
            }
        )
    }
}

object WoIconButtonDefaults {
    // TODO: File bug
    // IconToggleButton disabled container alpha not exposed by IconButtonDefaults
    const val DISABLED_ICON_BUTTON_CONTAINER_ALPHA = 0.12f
}
