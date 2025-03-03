package com.wceng.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.wceng.model.Translate
import com.wceng.model.UserTranslate

class UserTranslatePreviewParameterProvider : PreviewParameterProvider<UserTranslate> {

    val translate = Translate(
        id = "1",
        originalLanguageCode = "en",
        targetLanguageCode = "zh",
        originalText = "Hello",
        targetText = "你好"
    )

    override val values: Sequence<UserTranslate>
        get() = sequenceOf(
            UserTranslate(
                translate = translate,
                originalLanguageText = "英文",
                targetLanguageText = "中文（简体）",
                collected = false
            )
        )
}