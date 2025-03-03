package com.wceng.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.wceng.model.Language

class LanguagesPreviewParameterProvider : PreviewParameterProvider<List<Language>> {
    override val values: Sequence<List<Language>>
        get() = sequenceOf(LanguagesTestData.languages)
}

object LanguagesTestData {

    val languages = listOf(
        Language(
            languageText = "中文",
            languageCode = "zh"
        ),
        Language(
            languageText = "英文",
            languageCode = "en"
        ),
        Language(
            languageText = "阿尔巴尼亚语",
            languageCode = "sq"
        ),
        Language(
            languageText = "法语",
            languageCode = "fr"
        ),
    )
}