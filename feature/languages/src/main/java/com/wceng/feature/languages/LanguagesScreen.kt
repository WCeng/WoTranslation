package com.wceng.feature.languages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.core.ui.DevicePreviews
import com.wceng.core.ui.LanguagesPreviewParameterProvider
import com.wceng.core.ui.listSafeFooter
import com.wceng.model.Language

@Composable
internal fun LanguagesScreen(
    viewModel: LanguagesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {

    val autoDetectLanguageUiState by viewModel.autoDetectLanguage.collectAsStateWithLifecycle()
    val currentLanguageUiState by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val languagesUiState by viewModel.languagesUiState.collectAsStateWithLifecycle()

    LanguagesScreen(
        isOriginalLanguages = viewModel.isOriginalLanguages,
        autoDetectLanguageUiState = autoDetectLanguageUiState,
        currentLanguageUiState = currentLanguageUiState,
        languagesUiState = languagesUiState,
        onBackClick = onBackClick,
        onLanguageSelected = {
            viewModel.selectLanguage(it)
            onBackClick()
        }
    )
}

@Composable
internal fun LanguagesScreen(
    isOriginalLanguages: Boolean,
    autoDetectLanguageUiState: AutoDetectLanguageUiState,
    currentLanguageUiState: CurrentLanguageUiState,
    languagesUiState: LanguagesUiState,
    onBackClick: () -> Unit,
    onLanguageSelected: (Language) -> Unit
) {

    Column {
        LanguagesTopAppBar(
            isOriginalLanguages = isOriginalLanguages, onBackClick = onBackClick
        )

        LazyColumn {
            when (autoDetectLanguageUiState) {
                AutoDetectLanguageUiState.Hide -> Unit
                is AutoDetectLanguageUiState.Shown -> autoDetectLanguageRow {
                    onLanguageSelected(
                        autoDetectLanguageUiState.language
                    )
                }
            }

            when (languagesUiState) {
                LanguagesUiState.Loading,
                LanguagesUiState.Error,
                    -> Unit

                is LanguagesUiState.Success -> {
                    if (currentLanguageUiState is CurrentLanguageUiState.Success) {
                        item {
                            Spacer(Modifier.height(8.dp))
                        }

                        languagesHeadLine(title = R.string.feature_languages_recent_languages)

                        recentLanguages(
                            data = languagesUiState.recentLanguagesUsed,
                            currentLanguage = currentLanguageUiState.current,
                            onLanguageSelected = onLanguageSelected
                        )

                        item {
                            Spacer(Modifier.height(8.dp))
                        }

                        languagesHeadLine(title = R.string.feature_languages_all_language)

                        allLanguages(
                            data = languagesUiState.allLanguages,
                            currentLanguage = currentLanguageUiState.current,
                            onLanguageSelected = onLanguageSelected
                        )

                        listSafeFooter()
                    }
                }
            }
        }


    }
}

private fun LazyListScope.autoDetectLanguageRow(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    item {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.feature_languages_auto_detect_language),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            },
            modifier = modifier.clickable {
                onClick()
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
        )
    }
}

private fun LazyListScope.languagesHeadLine(modifier: Modifier = Modifier, title: Int) {
    item {
        Text(
            text = stringResource(title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .padding(16.dp)
        )
    }
}

private fun LazyListScope.recentLanguages(
    data: List<Language>, currentLanguage: Language, onLanguageSelected: (Language) -> Unit
) {
    items(items = data, key = { "recent_${it.languageCode}" },
        contentType = { "recent" }) {
        LanguageItem(
            language = it, selected = it == currentLanguage, onClick = onLanguageSelected
        )
    }
}

private fun LazyListScope.allLanguages(
    data: List<Language>, currentLanguage: Language, onLanguageSelected: (Language) -> Unit
) {
    items(
        items = data,
        key = { "all_${it.languageCode}" },
        contentType = { "all" }
    ) {
        LanguageItem(
            language = it, selected = it == currentLanguage, onClick = onLanguageSelected
        )
    }
}

@Composable
private fun LanguageItem(
    modifier: Modifier = Modifier,
    language: Language,
    selected: Boolean,
    onClick: (Language) -> Unit
) {
    ListItem(
        modifier = modifier.clickable {
            onClick(language)
        },
        headlineContent = {
            Text(text = language.languageText)
        },
        leadingContent = {
            if (selected) Icon(
                imageVector = Icons.Rounded.Check, contentDescription = null,
                modifier = Modifier.testTag("languages:checked")
            )
        },
    )
}

@DevicePreviews
@Composable
fun LanguagesScreenSelectOriginalLanguage(
    @PreviewParameter(LanguagesPreviewParameterProvider::class)
    data: List<Language>
) {
    Surface  {
        LanguagesScreen(
            isOriginalLanguages = true,
            autoDetectLanguageUiState = AutoDetectLanguageUiState.Shown(
                Language("自动检测", "auto")
            ),
            currentLanguageUiState = CurrentLanguageUiState.Success(
                Language("英文", "en")
            ),
            languagesUiState = LanguagesUiState.Success(
                recentLanguagesUsed = data.take(2),
                allLanguages = data
            ),
            onBackClick = {},
            onLanguageSelected = {}
        )
    }
}

@DevicePreviews
@Composable
fun LanguagesScreenSelectTargetLanguage(
    @PreviewParameter(LanguagesPreviewParameterProvider::class)
    data: List<Language>
) {
    Surface  {
        LanguagesScreen(
            isOriginalLanguages = false,
            autoDetectLanguageUiState = AutoDetectLanguageUiState.Hide,
            currentLanguageUiState = CurrentLanguageUiState.Success(
                Language("英文", "en")
            ),
            languagesUiState = LanguagesUiState.Success(
                recentLanguagesUsed = data.take(2),
                allLanguages = data
            ),
            onBackClick = {},
            onLanguageSelected = {}
        )
    }


}