package com.wceng.feature.translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wceng.core.designsystem.components.CardRectAngle
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.core.ui.CollectToggleIconButton

@Composable
internal fun InputTextCard(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    val clickActionLabel = stringResource(R.string.feature_translate_input_text)

    CardRectAngle(
        modifier = modifier
            .semantics {
                onClick(label = clickActionLabel, action = null)
            },
        onClick = onClick,
    ) {

        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.feature_translate_text_field_place_holder)) },
            enabled = false
        )
    }
}

@Composable
internal fun TranslateOriginalInfoCard(
    modifier: Modifier = Modifier,
    originalLanguageText: String = "",
    originalText: String = "",
    onClose: () -> Unit = {},
    onReadAloud: () -> Unit = {},
    onOriginalTextClickWithIndex: (Int) -> Unit = {},
) {
    CardRectAngle(
        modifier = modifier,
    ) {
        Column {
            TranslateLanguageTextRow(
                languageText = originalLanguageText,
                endIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.feature_translate_close)
                        )
                    }
                },
                onReadAloud = onReadAloud
            )
            Spacer(Modifier.height(8.dp))
            TranslateOriginalText(
                text = originalText,
                style = MaterialTheme.typography.headlineMedium,
                onTextIndex = onOriginalTextClickWithIndex,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun TranslateTargetInfoCard(
    modifier: Modifier = Modifier,
    targetLanguageText: String = "",
    collected: Boolean = false,
    targetText: String = "",
    onToggleCollect: () -> Unit = {},
    onCopy: () -> Unit = {},
    onReadAloud: () -> Unit = {}
) {
    CardRectAngle(
        modifier = modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Column {
            TranslateLanguageTextRow(
                languageText = targetLanguageText,
                endIcon = {
                    CollectToggleIconButton(
                        collected = collected,
                        onToggleAction = onToggleCollect
                    )
                },
                onReadAloud = onReadAloud
            )
            Spacer(Modifier.height(8.dp))
            Text(text = targetText, style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onCopy) {
                    Icon(
                        imageVector = Icons.Filled.CopyAll,
                        contentDescription = stringResource(R.string.feature_translate_copy)
                    )
                }
            }
        }
    }
}


@Composable
private fun TranslateLanguageTextRow(
    modifier: Modifier = Modifier,
    languageText: String,
    endIcon: @Composable () -> Unit,
    onReadAloud: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onReadAloud) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = stringResource(R.string.feature_translate_read_original_text_aloud)
                )
            }
            Text(text = languageText, style = MaterialTheme.typography.labelLarge)
        }

        endIcon()
    }
}

@Preview
@Composable
private fun InputTextCardPreview() {
    WoTheme {
        InputTextCard(onClick = {})
    }
}

@Preview
@Composable
private fun TranslateOriginalInfoCardPreview() {
    WoTheme {
        TranslateOriginalInfoCard(
            originalLanguageText = "中文",
            originalText = "你好"
        )
    }
}

@Preview
@Composable
private fun TranslateTargetInfoCardDiscardPreview() {
    WoTheme {
        TranslateTargetInfoCard(
            targetLanguageText = "英文",
            collected = false,
            targetText = "Hello"
        )
    }
}

@Preview
@Composable
private fun TranslateTargetInfoCardCollectPreview() {
    WoTheme {
        TranslateTargetInfoCard(
            targetLanguageText = "英文",
            collected = true,
            targetText = "Hello"
        )
    }
}