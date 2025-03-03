package com.wceng.feature.translate

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle

@Composable
internal fun TranslateOriginalText(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier,
    onTextIndex: (Int) -> Unit
) {

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = text,
        style = style,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    textLayoutResult?.let { layoutResult ->
                        val clickedCharIndex = layoutResult.getOffsetForPosition(offset)
                        onTextIndex(clickedCharIndex)
                    }
                }
            },
        onTextLayout = { layoutResult ->
            textLayoutResult = layoutResult
        }
    )
}