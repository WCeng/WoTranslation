package com.wceng.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.wceng.core.testing.data.translatesTestData
import com.wceng.model.Translate

class TranslatesPreviewParameterProvider : PreviewParameterProvider<List<Translate>> {
    override val values: Sequence<List<Translate>>
        get() = sequenceOf(translatesTestData)
}