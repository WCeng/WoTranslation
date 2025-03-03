package com.wceng.feature.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.wceng.core.designsystem.components.CardRectAngle
import com.wceng.core.ui.TranslatesPreviewParameterProvider
import com.wceng.model.Translate

internal fun LazyListScope.recentTranslateList(
    data: List<Translate>, onItemClick: (Translate) -> Unit
) {
    items(items = data, key = { it.id }) {
        RecentTranslate(item = it, onClick = { onItemClick(it) })
    }
}

@Composable
internal fun RecentTranslate(item: Translate, onClick: () -> Unit) {
    CardRectAngle(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.History, contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = item.originalText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.targetText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecentTranslateListPreview(
    @PreviewParameter(provider = TranslatesPreviewParameterProvider::class)
    translates: List<Translate>
) {
    Surface {
        LazyColumn {
            recentTranslateList(data = translates, onItemClick = {})
        }
    }
}

@Preview
@Composable
private fun RecentTranslatePreview(
    @PreviewParameter(provider = TranslatesPreviewParameterProvider::class)
    translates: List<Translate>
) {
    Surface {
        RecentTranslate(
            item = translates[1],
            onClick = {}
        )
    }
}

