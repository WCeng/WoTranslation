package com.wceng.model

data class UserTranslate(
    val translate: Translate,
    val originalLanguageText: String,
    val targetLanguageText: String,
    val collected: Boolean
)
