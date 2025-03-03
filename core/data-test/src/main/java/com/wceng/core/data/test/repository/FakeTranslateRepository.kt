package com.wceng.core.data.test.repository

import com.wceng.core.testing.data.translatesTestData
import com.wceng.data.repository.TranslateRepository
import com.wceng.model.Translate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class FakeTranslateRepository @Inject constructor() : TranslateRepository {

    private val testData = mutableListOf(*translatesTestData.toTypedArray())

    override fun getTranslate(id: String): Flow<Translate> {
        return getTranslates().map { translates ->
            translates.first { it.id == id }
        }
    }

    override fun getTranslate(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String,
        isOriginalLanguageAutoDetect: Boolean
    ): Flow<Translate> {
        return getTranslates()
            .map { translates ->
                translates.first {
                    it.originalText == originalText
                            && it.originalLanguageCode == originalLanguageCode
                            && it.targetLanguageCode == targetLanguageCode
                }
            }
    }

    override fun getTranslates(): Flow<List<Translate>> {
        return flow {
            emit(testData)
        }
    }

    override fun getTranslates(ids: Set<String>): Flow<List<Translate>> {
        return getTranslates()
            .map { translates ->
                translates.filter {
                    it.id in ids
                }
            }
    }

    override fun getLatestTranslates(limit: Int): Flow<List<Translate>> {
        return getTranslates().map { translates ->
            translates.reversed().take(limit)
        }
    }

    override suspend fun getTranslateFromNetwork(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String
    ): Translate {
        return Translate(
            id = "idFromNetwork",
            originalLanguageCode = "targetLanguageCodeFromNetwork",
            targetLanguageCode = "targetLanguageCodeFromNetwork",
            originalText = "originalTextFromNetwork",
            targetText = "targetTextFromNetwork"
        )
    }

    override suspend fun deleteTranslate(translateId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertTranslate(translate: Translate) {
        TODO("Not yet implemented")
    }

}