package com.wceng.core.testing.data

import com.wceng.model.Translate
import com.wceng.model.UserTranslate


val translatesTestData = listOf(
    Translate(
        id = "1",
        originalLanguageCode = "en",
        targetLanguageCode = "zh",
        originalText = "Hello",
        targetText = "你好"
    ),
    Translate(
        id = "2",
        originalLanguageCode = "zh",
        targetLanguageCode = "en",
        originalText = "你好！很高兴见到你，有什么我可以帮忙的吗？无论是关于某个话题的疑问，还是需要一些建议，我都乐意为你提供帮助。\uD83D\uDE0A\n",
        targetText = "Hello! Nice to meet you, is there anything I can do to help? Whether you have a question about a topic or need some advice, I'm here to help. \uD83D\uDE0A"
    ),
    Translate(
        id = "3",
        originalLanguageCode = "en",
        targetLanguageCode = "zh",
        originalText = "What",
        targetText = "什么"
    )
)

val defaultUserTranslate = translatesTestData[1].let { translate ->
    UserTranslate(
        translate = translate,
        originalLanguageText = defaultChineseLanguage.languageText,
        targetLanguageText = defaultEnglishLanguage.languageText,
        collected = false
    )
}
