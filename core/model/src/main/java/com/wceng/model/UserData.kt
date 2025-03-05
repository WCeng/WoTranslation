package com.wceng.model

data class UserData constructor(
    val languagePreferences: LanguagePreferences,
    val shouldHideOnboarding: Boolean,
    val collectedTranslates: Set<String>,
    val recentOriginalLanguageCodes: List<String>,
    val recentTargetLanguageCodes: List<String>,
    val darkThemeConfig: DarkThemeConfig
)
