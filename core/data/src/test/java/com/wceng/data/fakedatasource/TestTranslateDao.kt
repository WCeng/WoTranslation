package com.wceng.data.fakedatasource

import com.wceng.database.dao.TranslateDao
import com.wceng.database.model.TranslateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestTranslateDao : TranslateDao {

    private val entitiesFlow = MutableStateFlow(emptyList<TranslateEntity>())

    override suspend fun insertOrUpdateTranslateEntity(trans: TranslateEntity) {
        entitiesFlow.update { old ->
            (listOf(trans) + old).distinctBy(TranslateEntity::id)
        }
    }

    override suspend fun deleteTranslateEntityById(id: String) {
        entitiesFlow.update { value ->
            value.filterNot { it.id == id }
        }
    }

    override fun getTranslateEntityBySrcAndCode(
        originalText: String,
        originalLanguageCode: String,
        targetLanguageCode: String
    ): Flow<TranslateEntity?> {
        return entitiesFlow.map { value ->
            value.firstOrNull {
                it.originalText == originalText
                        && it.originalLanguageCode == originalLanguageCode
                        && it.targetLanguageCode == targetLanguageCode
            }
        }
    }

    override fun getTranslateEntityById(id: String): Flow<TranslateEntity> {
        return entitiesFlow.map { value ->
            value.first { it.id == id }
        }
    }

    override fun getTranslateEntities(): Flow<List<TranslateEntity>> {
        return entitiesFlow
    }

    override fun getTranslateEntities(ids: Set<String>): Flow<List<TranslateEntity>> {
        return entitiesFlow.map { entities ->
            entities.filter { it.id in ids }
        }
    }

    override fun getLatestTranslateEntities(limit: Int): Flow<List<TranslateEntity>> {
        return entitiesFlow.map { entities ->
            entities.sortedByDescending { it.translatedDate }.take(limit)
        }
    }

    override fun isTranslateEntityExists(
        originalText: String,
        originalLanguageCode: String,
        targetLanguageCode: String
    ): Flow<Boolean> {
        return entitiesFlow.map { values ->
            values.any {
                it.originalText == originalText
                        && it.originalLanguageCode == originalLanguageCode
                        && it.targetLanguageCode == targetLanguageCode
            }
        }
    }
}