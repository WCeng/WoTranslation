package com.wceng.core.testing.repository

import com.wceng.data.repository.TranslateRepository
import com.wceng.model.Translate
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestTranslateRepository : TranslateRepository {


    private val _savedTranslates = MutableSharedFlow<List<Translate>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val currentTranslates
        get() = _savedTranslates.replayCache.firstOrNull() ?: emptyList()

    override fun getTranslate(id: String): Flow<Translate> =
        _savedTranslates.map { list ->
            list.first { it.id == id }
        }

    override fun getTranslate(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String,
        isOriginalLanguageAutoDetect: Boolean
    ): Flow<Translate> =
        _savedTranslates.map { list ->
            list.first {
                it.originalLanguageCode == originalLanguageCode
                        && it.targetLanguageCode == targetLanguageCode
                        && it.originalText == originalText
            }
        }

    override fun getTranslates(): Flow<List<Translate>> = _savedTranslates

    override fun getTranslates(ids: Set<String>): Flow<List<Translate>> {
        return _savedTranslates.map { translates ->
            translates.filter { it.id in ids }
        }
    }

    override fun getLatestTranslates(limit: Int): Flow<List<Translate>> {
        return _savedTranslates.map { translates ->
            translates.reversed().take(limit)
        }
    }

    override suspend fun getTranslateFromNetwork(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String
    ): Translate {
        return Translate(
            id = "networkId",
            originalLanguageCode = "networkOriginalLanguageCode",
            targetLanguageCode = "networkTargetLanguageCode",
            originalText = "networkOriginalText",
            targetText = "networkTargetText"
        )
    }

    override suspend fun deleteTranslate(translateId: String) {
        currentTranslates.let { translates ->
            val result = translates.filterNot { it.id == translateId }
            _savedTranslates.tryEmit(result)
        }
    }

    override suspend fun upsertTranslate(translate: Translate) {
        currentTranslates.let { translates ->
            val result = (listOf(translate) + translates).distinct()
            _savedTranslates.tryEmit(result)
        }
    }

    fun sendTranslates(translates: List<Translate>) {
        _savedTranslates.tryEmit(translates)
    }
}