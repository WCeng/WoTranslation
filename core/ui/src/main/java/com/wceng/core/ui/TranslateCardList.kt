package com.wceng.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.wceng.model.CollectableTranslate

@SuppressLint("ModifierParameter")
fun LazyListScope.translateCardList(
    items: List<CollectableTranslate>,
    onTranslateClick: (CollectableTranslate) -> Unit,
    onToggleCollect: (CollectableTranslate) -> Unit,
    itemModifier: Modifier = Modifier
) = items(
    items = items,
    key = { it.translate.id },
    itemContent = { item ->
        TranslateCard(
            collectableTranslate = item,
            onToggleCollect = { onToggleCollect(item) },
            onClick = { onTranslateClick(item) },
            modifier = itemModifier
        )
    })