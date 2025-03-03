package com.wceng.model

data class Translate(
    val id: String,
    val originalLanguageCode: String,
    val targetLanguageCode: String,
    val originalText: String,
    val targetText: String,
)
