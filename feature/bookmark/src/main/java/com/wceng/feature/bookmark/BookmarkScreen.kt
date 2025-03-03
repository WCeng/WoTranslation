package com.wceng.feature.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wceng.core.ui.listSafeFooter
import com.wceng.core.ui.translateCardList

@Composable
internal fun BookmarksScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    onTranslateClick: (String) -> Unit,
    onMenuClick: () -> Unit
) {

    val uiState by viewModel.bookmarkUiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    BookmarksContent(
        bookmarkUiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::changeSearchQuery,
        onMenuClick = onMenuClick,
        onTranslateClick = onTranslateClick,
        onToggleCollect = viewModel::collectTranslate
    )
}

@Composable
internal fun BookmarksContent(
    modifier: Modifier = Modifier,
    bookmarkUiState: BookmarkUiState,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onTranslateClick: (String) -> Unit = {},
    onToggleCollect: (String, Boolean) -> Unit = { _, _ -> },
) {
    Column(modifier = modifier) {
        BookmarksAppTopBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onMenuClick = onMenuClick,
        )

        when (bookmarkUiState) {
            BookmarkUiState.Loading -> BookmarksLoadingContent()

            BookmarkUiState.Error -> Unit

            BookmarkUiState.EmptyResult -> BookmarksEmptyResultContent()

            is BookmarkUiState.Success -> {
                LazyColumn {
                    translateCardList(
                        items = bookmarkUiState.data,
                        onTranslateClick = { onTranslateClick(it.translate.id) },
                        onToggleCollect = {
                            onToggleCollect(it.translate.id, !it.collected)
                        },
                    )
                    listSafeFooter()
                }
            }
        }
    }
}

@Composable
private fun BookmarksLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.testTag("bookmarks:loading"))
    }
}

@Composable
private fun BookmarksEmptyResultContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.feature_bookmarks_empty_result_description),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun BookmarksEmptyResultContentPreview() {
    Surface {
        BookmarksEmptyResultContent()
    }
}