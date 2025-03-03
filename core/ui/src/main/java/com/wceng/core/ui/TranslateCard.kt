package com.wceng.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.model.CollectableTranslate
import com.wceng.model.Translate

@Composable
fun TranslateCard(
    modifier: Modifier = Modifier,
    collectableTranslate: CollectableTranslate,
    onToggleCollect: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val clickActionLabel = stringResource(R.string.core_ui_translate_item)

    ListItem(headlineContent = {
        Text(
            text = collectableTranslate.translate.originalText,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }, supportingContent = {
        Text(
            text = collectableTranslate.translate.targetText,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }, trailingContent = {
        CollectToggleIconButton(
            collected = collectableTranslate.collected, onToggleAction = onToggleCollect
        )
    }, modifier = modifier
        .semantics {
            onClick(label = clickActionLabel, action = null)
        }
        .clickable { onClick() })
}

@Preview
@Composable
private fun TranslateCardCollectedPreview() {
    WoTheme {
        TranslateCard(
            collectableTranslate = collectableTranslate(
                id = "1", collected = true, originalText = "Hello", targetText = "你好"
            )
        )
    }
}

@Preview
@Composable
private fun TranslateCardDiscardPreview() {
    WoTheme {
        TranslateCard(
            collectableTranslate = collectableTranslate(
                id = "1", collected = false, originalText = "Hello", targetText = "你好"
            )
        )
    }
}

@Preview
@Composable
private fun TranslateCardTitleMuchLongPreview() {
    WoTheme {
        TranslateCard(
            collectableTranslate = collectableTranslate(
                id = "1",
                collected = false,
                originalText = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello",
                targetText = "你好"
            )
        )
    }
}

@Preview
@Composable
private fun TranslateCardContentMuchLongPreview() {
    WoTheme {
        TranslateCard(
            collectableTranslate = collectableTranslate(
                id = "1",
                collected = false,
                originalText = "Hello",
                targetText = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你" + "好你好你好你好你好你好你好"
            )
        )
    }
}


internal fun collectableTranslate(
    id: String, collected: Boolean, originalText: String = "", targetText: String = ""
) = CollectableTranslate(
    translate = Translate(
        id = id,
        originalText = originalText,
        originalLanguageCode = "",
        targetLanguageCode = "",
        targetText = targetText
    ), collected = collected
)
