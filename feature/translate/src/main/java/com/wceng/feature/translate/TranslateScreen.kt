package com.wceng.feature.translate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wceng.core.designsystem.components.CardRectAngle
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.core.ui.CollectedTranslateFeedsPreviewParams
import com.wceng.core.ui.DevicePreviews
import com.wceng.core.ui.UserLanguagesPreviewParameterProvider
import com.wceng.core.ui.UserTranslatePreviewParameterProvider
import com.wceng.core.ui.listSafeFooter
import com.wceng.core.ui.translateCardList
import com.wceng.model.CollectableTranslate
import com.wceng.model.Language
import com.wceng.model.Translate
import com.wceng.model.UserLanguages
import com.wceng.model.UserTranslate

@Composable
fun TranslateScreen(
    originalText: String? = null,
    viewModel: TranslateViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    onInputClick: (String?, Int?) -> Unit,
    onSelectLanguages: (Boolean) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onOriginalTextTranslated: () -> Unit
) {
    //接受并翻译来自InputScreen的源文本
    originalText?.let {
        LaunchedEffect(originalText, viewModel) {
            viewModel.translateText(originalText)
            onOriginalTextTranslated()
        }
    }

    val translateScreenUiState by viewModel.translateScreenUiState.collectAsStateWithLifecycle()
    val userLanguagesUiState by viewModel.userLanguagesUiState.collectAsStateWithLifecycle()
    val translateFeedUiState by viewModel.translateFeedUiState.collectAsStateWithLifecycle()
    val userTranslateUiState by viewModel.userTranslateUiState.collectAsStateWithLifecycle()

    TranslateContent(
        translateScreenUiState = translateScreenUiState,
        userLanguagesUiState = userLanguagesUiState,
        translateFeedUiState = translateFeedUiState,
        userTranslateUiState = userTranslateUiState,
        onClickMenu = onMenuClick,
        onOriginalLanguageClick = { onSelectLanguages(true) },
        onTargetLanguageClick = { onSelectLanguages(false) },
        onReverseLanguage = viewModel::reverseLanguage,
        onInputCardClick = { onInputClick(null, null) },
        onTranslateClick = viewModel::showTranslate,
        onToggleCollect = viewModel::collectTranslate,
        onTranslateRetry = viewModel::retryTranslate,
        onReadLanguageTextAloud = { },
        onOriginalTextClickWithIndex = onInputClick,
        onCloseTranslateResult = viewModel::closeTranslateResult,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
internal fun TranslateContent(
    translateScreenUiState: TranslateScreenUiState,
    userLanguagesUiState: UserLanguagesUiState,
    translateFeedUiState: TranslateFeedUiState,
    userTranslateUiState: UserTranslateUiState,
    onClickMenu: () -> Unit,
    onOriginalLanguageClick: () -> Unit,
    onTargetLanguageClick: () -> Unit,
    onReverseLanguage: () -> Unit,
    onInputCardClick: () -> Unit,
    onTranslateClick: (Translate) -> Unit,
    onToggleCollect: (String, Boolean) -> Unit,
    onTranslateRetry: () -> Unit,
    onReadLanguageTextAloud: (String) -> Unit,
    onOriginalTextClickWithIndex: (String, Int) -> Unit,
    onCloseTranslateResult: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    val context = LocalContext.current
    var textToCopy by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(textToCopy) {
        textToCopy?.let { text ->
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Text", text)
            clipboardManager.setPrimaryClip(clipData)
            onShowSnackbar(context.getString(R.string.feature_translate_copied))
            textToCopy = null
        }
    }

    val isUserLanguageLoading = userLanguagesUiState is UserLanguagesUiState.Loading
    val isTranslateFeedLoading = translateFeedUiState is TranslateFeedUiState.Loading

    ReportDrawnWhen { !isUserLanguageLoading && !isTranslateFeedLoading }

    LazyColumn {
        translateTopAppBar(onClickMenu = onClickMenu)

        translateUserLanguagesCard(
            userLanguagesUiState = userLanguagesUiState,
            onOriginalLanguageClick = onOriginalLanguageClick,
            onTargetLanguageClick = onTargetLanguageClick,
            onReverseLanguage = onReverseLanguage,
        )

        when (translateScreenUiState) {
            TranslateScreenUiState.Initialing -> {
                inputTextCard(onClick = onInputCardClick)
                translateFeeds(
                    translateFeedUiState = translateFeedUiState,
                    onTranslateClick = {
                        onTranslateClick(it.translate)
                    },
                    onToggleCollect = {
                        onToggleCollect(it.translate.id, !it.collected)
                    }
                )
            }

            is TranslateScreenUiState.Translating -> {
                userTranslateContent(
                    originalText = translateScreenUiState.originalText,
                    userTranslateUiState = userTranslateUiState,
                    onRetry = onTranslateRetry,
                    onToggleCollect = {
                        onToggleCollect(it.translate.id, !it.collected)
                    },
                    onCopy = {
                        textToCopy = it.translate.targetText
                    },
                    onReadAloud = onReadLanguageTextAloud,
                    onOriginalTextClickWithIndex = {
                        onOriginalTextClickWithIndex(translateScreenUiState.originalText, it)
                    },
                    onClose = onCloseTranslateResult,
                )
            }
        }

        listSafeFooter()
    }
}

private fun LazyListScope.translateUserLanguagesCard(
    userLanguagesUiState: UserLanguagesUiState,
    onOriginalLanguageClick: () -> Unit,
    onTargetLanguageClick: () -> Unit,
    onReverseLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (userLanguagesUiState) {
        UserLanguagesUiState.Loading, UserLanguagesUiState.Error -> Unit
        is UserLanguagesUiState.Success -> {
            item {
                val originalText = userLanguagesUiState.userLanguages.originalLanguage.languageText
                val targetText = userLanguagesUiState.userLanguages.targetLanguage.languageText
                CardRectAngle(modifier = modifier) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            DownIconButton(
                                text = originalText,
                                onClickAction = onOriginalLanguageClick,
                            )
                        }
                        IconButton(
                            onClick = onReverseLanguage,
                            enabled = userLanguagesUiState.canReverse,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                                contentDescription = stringResource(R.string.feature_translate_reverse_language)
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            DownIconButton(
                                text = targetText,
                                onClickAction = onTargetLanguageClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.translateFeeds(
    modifier: Modifier = Modifier,
    translateFeedUiState: TranslateFeedUiState,
    onTranslateClick: (CollectableTranslate) -> Unit,
    onToggleCollect: (CollectableTranslate) -> Unit,
) {
    when (translateFeedUiState) {
        TranslateFeedUiState.Error -> Unit
        TranslateFeedUiState.Loading -> {
            item {
                LoadingContent(modifier)
            }
        }

        is TranslateFeedUiState.Success -> {
            translateCardList(
                items = translateFeedUiState.translateFeeds,
                onTranslateClick = onTranslateClick,
                onToggleCollect = onToggleCollect,
            )
        }
    }

}

private fun LazyListScope.userTranslateContent(
    originalText: String,
    userTranslateUiState: UserTranslateUiState,
    onRetry: () -> Unit,
    onToggleCollect: (UserTranslate) -> Unit,
    onCopy: (UserTranslate) -> Unit,
    onReadAloud: (String) -> Unit,
    onOriginalTextClickWithIndex: (Int) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (userTranslateUiState) {
        is UserTranslateUiState.Loading -> {
            item {
                Column(modifier) {
                    TranslateOriginalInfoCard(
                        originalText = originalText,
                        onOriginalTextClickWithIndex = onOriginalTextClickWithIndex,
                        onClose = onClose
                    )
                    LoadingContent()
                }
            }
        }

        UserTranslateUiState.Error -> {
            item {
                Column(modifier) {
                    TranslateOriginalInfoCard(
                        originalText = originalText,
                        onOriginalTextClickWithIndex = onOriginalTextClickWithIndex,
                        onClose = onClose
                    )
                    TranslateFailContent(onRetry = onRetry)
                }
            }
        }

        is UserTranslateUiState.Success -> {
            item {
                Column(modifier) {
                    TranslateOriginalInfoCard(
                        originalText = userTranslateUiState.userTranslate.translate.originalText,
                        originalLanguageText = userTranslateUiState.userTranslate.originalLanguageText,
                        onReadAloud = { onReadAloud(userTranslateUiState.userTranslate.originalLanguageText) },
                        onClose = onClose,
                        onOriginalTextClickWithIndex = onOriginalTextClickWithIndex
                    )
                    TranslateTargetInfoCard(
                        targetText = userTranslateUiState.userTranslate.translate.targetText,
                        targetLanguageText = userTranslateUiState.userTranslate.targetLanguageText,
                        collected = userTranslateUiState.userTranslate.collected,
                        onToggleCollect = {
                            onToggleCollect(userTranslateUiState.userTranslate)
                        },
                        onCopy = {
                            onCopy(userTranslateUiState.userTranslate)
                        },
                        onReadAloud = {
                            onReadAloud(userTranslateUiState.userTranslate.targetLanguageText)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TranslateFailContent(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.feature_translate_fail))
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.feature_translate_retry))
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier.testTag("translate_loading_indicator"))
    }
}

fun LazyListScope.inputTextCard(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    item {
        InputTextCard(modifier, onClick)
    }
}

@Composable
private fun DownIconButton(
    text: String, onClickAction: () -> Unit, modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClickAction,
        modifier = modifier
    ) {
        Row {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(weight = 1f, fill = false)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown, contentDescription = null
            )
        }
    }

}

@DevicePreviews
@Composable
private fun TranslateUserLanguagesLoaded(
    @PreviewParameter(provider = UserLanguagesPreviewParameterProvider::class)
    userLanguages: UserLanguages
) {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Success(userLanguages),
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading,
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = { }
        )

    }
}

@DevicePreviews
@Composable
private fun TranslateUserLanguagesIsVeryLong(
    @PreviewParameter(provider = UserLanguagesPreviewParameterProvider::class)
    userLanguages: UserLanguages
) {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Success(
                UserLanguages(
                    originalLanguage = Language(
                        languageText = "英文英文英文英文英文英文英文英文英文英文",
                        languageCode = "en"
                    ),
                    targetLanguage = Language(
                        languageText = "中文",
                        languageCode = "zh"
                    )
                )
            ),
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading,
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = {}
        )

    }
}

@DevicePreviews
@Composable
private fun TranslateContentInitialingAndTranslateFeedsLoading(
) {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading,
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = { }
        )

    }
}

@DevicePreviews
@Composable
private fun TranslateContentInitialingAndTranslateFeedsLoaded(
    @PreviewParameter(CollectedTranslateFeedsPreviewParams::class)
    translateFeeds: List<CollectableTranslate>
) {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Initialing,
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Success(translateFeeds),
            userTranslateUiState = UserTranslateUiState.Loading,
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = { }
        )

    }
}

@DevicePreviews
@Composable
private fun TranslateContentTranslatingAndUserTranslateLoading() {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Translating(originalText = "hello"),
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Loading,
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = { }
        )

    }
}

@DevicePreviews
@Composable
private fun TranslateContentTranslatingAndUserTranslateSuccess(
    @PreviewParameter(UserTranslatePreviewParameterProvider::class)
    userTranslate: UserTranslate
) {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Translating(originalText = "nihao"),
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Success(userTranslate),
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = { }
        )

    }
}

@DevicePreviews
@Composable
private fun TranslateContentTranslatingAndUserTranslateError() {
    WoTheme {
        TranslateContent(
            translateScreenUiState = TranslateScreenUiState.Translating(originalText = "nihao"),
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateFeedUiState = TranslateFeedUiState.Loading,
            userTranslateUiState = UserTranslateUiState.Error,
            onClickMenu = { },
            onOriginalLanguageClick = { },
            onTargetLanguageClick = { },
            onReverseLanguage = { },
            onInputCardClick = { },
            onTranslateClick = { },
            onToggleCollect = { _, _ -> },
            onTranslateRetry = { },
            onReadLanguageTextAloud = { },
            onOriginalTextClickWithIndex = { _, _ -> },
            onCloseTranslateResult = { },
            onShowSnackbar = { }
        )

    }
}
