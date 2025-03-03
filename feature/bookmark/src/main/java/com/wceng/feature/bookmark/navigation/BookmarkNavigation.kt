package com.wceng.feature.bookmark.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wceng.feature.bookmark.BookmarksScreen
import kotlinx.serialization.Serializable

@Serializable
data object BookmarkRoute

fun NavHostController.navigationToBookmark(navOptions: NavOptions?) {
    navigate(route = BookmarkRoute, navOptions = navOptions)
}

fun NavGraphBuilder.bookmarkScreen(
    onTranslateClick: (String) -> Unit,
    onMenuClick: () -> Unit
) {
    composable<BookmarkRoute> {
        BookmarksScreen(
            onTranslateClick = onTranslateClick,
            onMenuClick = onMenuClick
        )
    }
}