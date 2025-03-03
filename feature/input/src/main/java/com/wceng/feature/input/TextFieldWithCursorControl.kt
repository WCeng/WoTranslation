package com.wceng.feature.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun TextFieldWithCursorControl() {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = "Hello, World!",
                selection = TextRange(5) // 初始光标位置在第 5 个字符后
            )
        )
    }

    TextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}