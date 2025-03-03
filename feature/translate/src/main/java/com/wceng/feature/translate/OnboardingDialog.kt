package com.wceng.feature.translate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wceng.model.Language

@Composable
internal fun OnboardingDialog(
    defaultOriginalLanguage: Language,
    defaultTargetLanguage: Language,
    languages: List<Language>,
    onClickDone: (Language, Language) -> Unit
) {
    var currentOriginalLanguage by remember { mutableStateOf(defaultOriginalLanguage) }

    var currentTargetLanguage by remember { mutableStateOf(defaultTargetLanguage) }

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = { onClickDone(currentOriginalLanguage, currentTargetLanguage) }) {
                Text(text = "完成")
            }
        },
        title = {
            Text(text = "设置您的语言偏好")
        },
        text = {
            Column {
                Text(text = "您的主要语言")
                LanguagesDownDropMenu(currentLanguage = currentOriginalLanguage,
                    languages = languages,
                    onSelectedLanguage = { currentOriginalLanguage = it })

                Spacer(Modifier.height(12.dp))
                Text(text = "您最常翻译的语言")
                LanguagesDownDropMenu(currentLanguage = currentTargetLanguage,
                    languages = languages,
                    onSelectedLanguage = { currentTargetLanguage = it })
            }
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagesDownDropMenu(
    modifier: Modifier = Modifier,
    currentLanguage: Language,
    languages: List<Language>,
    onSelectedLanguage: (Language) -> Unit

) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        TextField(
            value = currentLanguage.languageText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { language ->
                DropdownMenuItem(text = { Text(text = language.languageText) }, onClick = {
                    onSelectedLanguage(language)
                    expanded = false
                })
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingPanelPreview() {
    Surface {
        OnboardingDialog(
            defaultOriginalLanguage = Language(languageText = "英文", ""),
            defaultTargetLanguage = Language("中文", ""),
            languages = listOf(Language("英文", ""), Language("中文", ""), Language("法语", "")),
            onClickDone = { _, _ -> }
        )
    }
}
