package com.wceng.wotranslation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.House
import androidx.compose.ui.graphics.vector.ImageVector
import com.wceng.feature.bookmark.navigation.BookmarkRoute
import com.wceng.feature.translate.navigation.TranslateBaseRoute
import com.wceng.feature.translate.navigation.TranslateRoute
import com.wceng.wotranslation.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val label: Int,
    val icon: ImageVector,
    val checkedIcon: ImageVector,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route
) {

    TRANSLATE(
        label = R.string.app_main_page,
        icon = Icons.Rounded.Home,
        checkedIcon = Icons.Rounded.Home,
        route = TranslateRoute::class,
        baseRoute = TranslateBaseRoute::class
    ),

    BOOKMARK(
        label = R.string.app_book_mark,
        icon = Icons.Rounded.BookmarkBorder,
        checkedIcon = Icons.Rounded.Bookmark,
        route = BookmarkRoute::class,
    )
}