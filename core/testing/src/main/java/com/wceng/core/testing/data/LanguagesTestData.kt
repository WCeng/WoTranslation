package com.wceng.core.testing.data

import com.wceng.model.Language
import com.wceng.model.UserLanguages

val defaultChineseLanguage = Language(
    languageText = "中文（简体）",
    languageCode = "zh"
)

val defaultEnglishLanguage = Language(
    languageText = "英文",
    languageCode = "en"
)

val defaultAutoLanguage = Language(
    languageText = "自动检测",
    languageCode = "auto"
)

val defaultUserLanguages = UserLanguages(
    originalLanguage = defaultEnglishLanguage,
    targetLanguage = defaultChineseLanguage
)

val languagesTestData = listOf(defaultChineseLanguage, defaultEnglishLanguage)
