package com.wceng.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.wceng.core.ui.PreviewParameterData.collectedTranslates
import com.wceng.model.CollectableTranslate
import com.wceng.model.Translate

class CollectedTranslateFeedsPreviewParams : PreviewParameterProvider<List<CollectableTranslate>> {

    override val values: Sequence<List<CollectableTranslate>>
        get() = sequenceOf(collectedTranslates)
}

object PreviewParameterData {

    val collectedTranslates = listOf(
        CollectableTranslate(
            translate = Translate(
                id = "1",
                originalLanguageCode = "en",
                targetLanguageCode = "zh",
                originalText = "Hello",
                targetText = "你好"
            ),
            collected = false
        ),
        CollectableTranslate(
            translate = Translate(
                id = "2",
                originalLanguageCode = "zh",
                targetLanguageCode = "en",
                originalText = "你好",
                targetText = "Hello"
            ),
            collected = true
        ),
        CollectableTranslate(
            translate = Translate(
                id = "3",
                originalLanguageCode = "en",
                targetLanguageCode = "zh",
                originalText = "Hello",
                targetText = "你好"
            ),
            collected = false
        ),
    )
}