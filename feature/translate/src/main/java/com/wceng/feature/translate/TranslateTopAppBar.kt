package com.wceng.feature.translate

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wceng.core.designsystem.components.WoTopAppBar
import com.wceng.core.designsystem.theme.WoTheme

internal fun LazyListScope.translateTopAppBar(
    modifier: Modifier = Modifier,
    onClickMenu: () -> Unit = {}
) {
    item {
        WoTopAppBar(
            modifier = modifier,
            title = R.string.feature_translate_top_bar_title,
            navigationIcon = Icons.Default.Menu,
            navigationContentDescription = stringResource(R.string.feature_translate_menu),
            navigationIconClick = onClickMenu
        )
    }
}

@Preview
@Composable
private fun TranslateTopAppBarPreview() {
    WoTheme {
        LazyColumn {
            translateTopAppBar ()
        }
    }
}