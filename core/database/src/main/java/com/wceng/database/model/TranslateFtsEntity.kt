package com.wceng.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "translates_fts")
@Fts4(contentEntity = TranslateEntity::class)
data class TranslateFtsEntity(

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int,

    @ColumnInfo(name = "original_text")
    val originalText: String,

    @ColumnInfo(name = "original_language_code")
    val originalLanguageCode: String,

    @ColumnInfo(name = "target_language_code")
    val targetLanguageCode: String
)