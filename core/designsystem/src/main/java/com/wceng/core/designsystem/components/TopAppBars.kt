package com.wceng.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wceng.core.designsystem.theme.WoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    navigationIcon: ImageVector,
    navigationContentDescription: String,
    navigationIconClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit  ={},
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = navigationIconClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationContentDescription
                )
            }
        },
        actions = actions
    )
}

@Preview
@Composable
private fun WoTopAppBarPreview() {
    WoTheme {
        WoTopAppBar(
            title = android.R.string.untitled,
            navigationIcon = Icons.Default.Menu,
            navigationContentDescription = "",
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
            }
        )
    }
}