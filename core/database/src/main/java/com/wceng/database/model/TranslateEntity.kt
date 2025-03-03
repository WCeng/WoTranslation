package com.wceng.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wceng.model.Translate
import kotlinx.datetime.Instant

@Entity(tableName = "translates")
data class TranslateEntity(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "original_language_code")
    var originalLanguageCode: String,

    @ColumnInfo(name = "target_language_code")
    var targetLanguageCode: String,

    @ColumnInfo(name = "original_text")
    var originalText: String,

    @ColumnInfo(name = "target_text")
    var targetText: String,

    @ColumnInfo(name = "translated_date", defaultValue = "0")
    val translatedDate: Instant
)

fun TranslateEntity.asExternalModel() = Translate(
    id = id,
    originalLanguageCode = originalLanguageCode,
    targetLanguageCode = targetLanguageCode,
    originalText = originalText,
    targetText = targetText
)