@file:OptIn(ExperimentalMaterial3Api::class)

package com.wceng.feature.bookmark

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun BookmarksAppTopBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onMenuClick: () -> Unit
) {

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.feature_bookmarks_menu)
                )
            }
        },
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                placeholder = { Text(text = stringResource(R.string.feature_bookmarks_search_place_holder)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("bookmark:textField")
            )
        }
    )
}

@Preview
@Composable
private fun BookmarksAppTopBarHasSearchQueryPreview() {
    Surface {
        BookmarksAppTopBar(
            searchQuery = "Hello",
            onSearchQueryChanged = { },
            onMenuClick = { }
        )
    }
}

@Preview
@Composable
private fun BookmarksAppTopBarNoSearchQueryPreview() {
    Surface {
        BookmarksAppTopBar(
            searchQuery = "",
            onSearchQueryChanged = { },
            onMenuClick = { }
        )
    }
}