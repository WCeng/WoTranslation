package com.wceng.data.model

import com.wceng.database.model.TranslateEntity
import com.wceng.model.Translate
import com.wceng.network.model.NetworkTranslate
import kotlinx.datetime.Instant

fun NetworkTranslate.asTranslateEntity(
    translateId: String,
    originalText: String,
    updateDate: Instant
) =
    TranslateEntity(
        id = translateId,
        originalText = originalText,
        targetText = targetText,
        originalLanguageCode = originalLanguageCode,
        targetLanguageCode = targetLanguageCode,
        translatedDate = updateDate
    )

fun Translate.asTranslateEntity(updateDate: Instant) =
    TranslateEntity(
        id = id,
        originalLanguageCode = originalLanguageCode,
        targetLanguageCode = targetLanguageCode,
        originalText = originalText,
        targetText = targetText,
        translatedDate = updateDate
    )