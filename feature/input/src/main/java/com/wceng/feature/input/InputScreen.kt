package com.wceng.feature.input

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRightAlt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wceng.core.designsystem.components.CardRectAngle
import com.wceng.core.designsystem.theme.WoTheme
import com.wceng.core.ui.DevicePreviews
import com.wceng.core.ui.TranslatesPreviewParameterProvider
import com.wceng.core.ui.UserLanguagesPreviewParameterProvider
import com.wceng.core.ui.listSafeFooter
import com.wceng.model.Translate
import com.wceng.model.UserLanguages

@Composable
internal fun InputScreen(
    onTranslateText: (String) -> Unit,
    viewModel: InputViewModel = hiltViewModel()
) {

    val inputTextValue by viewModel.inputTextValue.collectAsStateWithLifecycle()
    val userLanguagesUiState by viewModel.userLanguagesUiState.collectAsStateWithLifecycle()
    val translateUiState by viewModel.translateUiState.collectAsStateWithLifecycle()
    val recentTranslatesUiState by viewModel.recentTranslatesUiState.collectAsStateWithLifecycle()

    InputScreen(
        inputTextValue = inputTextValue,
        userLanguagesUiState = userLanguagesUiState,
        translateUiState = translateUiState,
        recentTranslatesUiState = recentTranslatesUiState,
        onTextFieldValueChange = viewModel::changeInputTextValue,
        onTranslateText = onTranslateText,
        onClearInputText = viewModel::clearInputText,
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
    )
}

@Composable
internal fun InputScreen(
    modifier: Modifier = Modifier,
    inputTextValue: TextFieldValue,
    userLanguagesUiState: UserLanguagesUiState,
    translateUiState: TranslateUiState,
    recentTranslatesUiState: RecentTranslatesUiState,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onTranslateText: (String) -> Unit,
    onClearInputText: () -> Unit
) {

    BackHandler {
        onTranslateText(inputTextValue.text)
    }

    Column(modifier = modifier) {
        UserInputFieldCard(
            inputTextValue = inputTextValue,
            userLanguagesUiState = userLanguagesUiState,
            translateUiState = translateUiState,
            onTextFieldValueChange = onTextFieldValueChange,
            onTranslateText = { onTranslateText(inputTextValue.text) },
            onClearInputText = onClearInputText
        )

        Spacer(Modifier.height(8.dp))

        RecentTranslates(
            uiState = recentTranslatesUiState,
            onTranslateClick = { onTranslateText(it.originalText) }
        )
    }
}

@Composable
private fun UserInputFieldCard(
    modifier: Modifier = Modifier,
    inputTextValue: TextFieldValue,
    userLanguagesUiState: UserLanguagesUiState,
    translateUiState: TranslateUiState,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onTranslateText: () -> Unit,
    onClearInputText: () -> Unit
) {
    when (userLanguagesUiState) {
        UserLanguagesUiState.Loading -> Unit

        is UserLanguagesUiState.Success -> {
            CardRectAngle(modifier = modifier) {
                Column {
                    TextInputField(
                        inputTextValue = inputTextValue,
                        placeholder = userLanguagesUiState.userLanguages.originalLanguage.languageText,
                        onTextFieldValueChange = onTextFieldValueChange,
                        onClearInputText = onClearInputText,
                        onTranslateText = onTranslateText
                    )

                    TranslateResultPreviewRow(
                        placeholder = userLanguagesUiState.userLanguages.targetLanguage.languageText,
                        onClick = onTranslateText,
                        translateUiState = translateUiState,
                    )
                }
            }
        }
    }
}

@Composable
private fun TextInputField(
    inputTextValue: TextFieldValue,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onClearInputText: () -> Unit,
    onTranslateText: () -> Unit,
    placeholder: String
) {
    val focusRequester = remember { FocusRequester() }

    //todo
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = inputTextValue,
        onValueChange = onTextFieldValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onTranslateText()
            }
        ),
        trailingIcon = {
            IconButton(onClick = onClearInputText) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.feature_input_close_text)
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(
                    R.string.feature_input_original_place_holder,
                    placeholder
                )
            )
        },

        )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun TranslateResultPreviewRow(
    placeholder: String,
    translateUiState: TranslateUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val text = when (translateUiState) {
        TranslateUiState.EmptyInput,
        TranslateUiState.Error,
            -> ""

        TranslateUiState.Loading -> stringResource(R.string.feature_input_loading)

        is TranslateUiState.Success -> translateUiState.translate.targetText
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
    ) {
        if (text.isEmpty()) {
            Text(
                text = stringResource(R.string.feature_input_target_place_holder, placeholder),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        } else {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                modifier = Modifier.testTag("translateResultTrailingIcon"),
                imageVector = Icons.AutoMirrored.Rounded.ArrowRightAlt,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun RecentTranslates(
    modifier: Modifier = Modifier,
    uiState: RecentTranslatesUiState,
    onTranslateClick: (Translate) -> Unit
) {

    when (uiState) {
        RecentTranslatesUiState.Loading,
        RecentTranslatesUiState.NotShown,
            -> Unit

        is RecentTranslatesUiState.Success -> {
            LazyColumn(modifier = modifier.testTag("RecentTranslateLazyColumn")) {
                recentTranslateList(data = uiState.translates, onItemClick = onTranslateClick)

                listSafeFooter()
            }
        }
    }
}

@Preview
@Composable
private fun UserInputFieldCardEmptyInputText(
    @PreviewParameter(UserLanguagesPreviewParameterProvider::class)
    userLanguages: UserLanguages
) {
    WoTheme {
        UserInputFieldCard(
            inputTextValue = TextFieldValue(text = ""),
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages
            ),
            translateUiState = TranslateUiState.EmptyInput,
            onTranslateText = { },
            onClearInputText = { },
            onTextFieldValueChange = {},
        )
    }
}

@Preview
@Composable
private fun UserInputFieldCardPopulateInputTextAndTranslateResult(
    @PreviewParameter(UserLanguagesPreviewParameterProvider::class)
    userLanguages: UserLanguages
) {
    WoTheme {
        UserInputFieldCard(
            inputTextValue = TextFieldValue(text = "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好"),
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages
            ),
            translateUiState = TranslateUiState.Success(
                Translate(
                    id = "1",
                    originalLanguageCode = "zh",
                    targetLanguageCode = "en",
                    originalText = "你好",
                    targetText = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHello"
                )
            ),
            onTranslateText = { },
            onClearInputText = { },
            onTextFieldValueChange = {},
        )
    }
}

@Preview
@Composable
private fun UserInputFieldCardTranslateResultLoading(
    @PreviewParameter(UserLanguagesPreviewParameterProvider::class)
    userLanguages: UserLanguages
) {
    WoTheme {
        UserInputFieldCard(
            userLanguagesUiState = UserLanguagesUiState.Success(
                userLanguages
            ),
            translateUiState = TranslateUiState.Loading,
            onTranslateText = { },
            onClearInputText = { },
            inputTextValue = TextFieldValue(),
            onTextFieldValueChange = {},
        )
    }
}

@DevicePreviews
@Composable
private fun InputScreenPopulateTranslates(
    @PreviewParameter(TranslatesPreviewParameterProvider::class)
    translates: List<Translate>
) {

    WoTheme {
        InputScreen(
            inputTextValue = TextFieldValue(),
            userLanguagesUiState = UserLanguagesUiState.Loading,
            translateUiState = TranslateUiState.EmptyInput,
            recentTranslatesUiState = RecentTranslatesUiState.Success(translates),
            onTranslateText = {},
            onClearInputText = {},
            onTextFieldValueChange = {}
        )
    }

}





