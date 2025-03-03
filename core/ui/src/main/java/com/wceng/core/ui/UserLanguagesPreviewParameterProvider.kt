package com.wceng.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.wceng.model.Language
import com.wceng.model.UserLanguages

class UserLanguagesPreviewParameterProvider : PreviewParameterProvider<UserLanguages> {
    override val values: Sequence<UserLanguages>
        get() = sequenceOf(
            UserLanguages(
                originalLanguage = Language(
                    languageCode = "zh",
                    languageText = "中文（简体）"
                ),
                targetLanguage = Language(
                    languageCode = "en",
                    languageText = "英文"
                )
            )
        )
}