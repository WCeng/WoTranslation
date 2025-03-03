package com.wceng.data.repository

import com.wceng.model.Translate
import kotlinx.coroutines.flow.Flow

interface TranslateRepository {

    fun getTranslate(id: String): Flow<Translate>

    fun getTranslate(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String,
        isOriginalLanguageAutoDetect: Boolean = false
    ): Flow<Translate>

    fun getTranslates(): Flow<List<Translate>>

    fun getLatestTranslates(limit: Int): Flow<List<Translate>>

    fun getTranslates(ids: Set<String>): Flow<List<Translate>>

    suspend fun getTranslateFromNetwork(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String
    ): Translate

    suspend fun deleteTranslate(translateId: String)

    suspend fun upsertTranslate(translate: Translate)
}