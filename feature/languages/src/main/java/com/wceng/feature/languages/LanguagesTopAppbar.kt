package com.wceng.feature.languages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wceng.core.designsystem.components.WoTopAppBar

@Composable
internal fun LanguagesTopAppBar(
    modifier: Modifier = Modifier,
    isOriginalLanguages: Boolean,
    onBackClick: () -> Unit
) {
    WoTopAppBar(
        modifier = modifier,
        title =
        if (isOriginalLanguages)
            R.string.feature_languages_original_title
        else
            R.string.feature_languages_target_title,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        navigationContentDescription = stringResource(R.string.feature_languages_back),
        navigationIconClick = onBackClick
    )
}

@Preview
@Composable
private fun OriginalLanguageTopAppbar() {
    LanguagesTopAppBar(
        isOriginalLanguages = true,
        onBackClick = {}
    )
}

@Preview
@Composable
private fun TargetLanguageTopAppbar() {
    LanguagesTopAppBar(
        isOriginalLanguages = false,
        onBackClick = {}
    )
}