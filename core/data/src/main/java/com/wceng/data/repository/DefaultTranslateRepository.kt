package com.wceng.data.repository

import com.wceng.data.model.asTranslateEntity
import com.wceng.database.dao.TranslateDao
import com.wceng.database.model.TranslateEntity
import com.wceng.database.model.asExternalModel
import com.wceng.model.Translate
import com.wceng.network.api.TranslateApi
import com.wceng.network.model.NetworkTranslate
import com.wceng.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class DefaultTranslateRepository
@Inject constructor(
    private val translateDao: TranslateDao,
    private val translateApi: TranslateApi,
) : TranslateRepository {

    override fun getTranslate(id: String): Flow<Translate> =
        translateDao.getTranslateEntityById(id).map(TranslateEntity::asExternalModel)

    override fun getTranslate(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String,
        isOriginalLanguageAutoDetect: Boolean
    ): Flow<Translate> = flow {
        var networkTranslate: NetworkTranslate? = null

        val realOriginalLanguageCode = isOriginalLanguageAutoDetect.let {
            if (it) {
                networkTranslate = translateApi.translateText(
                    originalLanguageCode,
                    targetLanguageCode,
                    originalText
                )
                networkTranslate?.originalLanguageCode
                    ?: throw Exception("networkTranslate is null")
            } else
                originalLanguageCode
        }

        val isCached = translateDao.isTranslateEntityExists(
            originalText,
            realOriginalLanguageCode,
            targetLanguageCode
        ).first()

        if (!isCached) {
            if (networkTranslate == null) {
                networkTranslate = translateApi.translateText(
                    realOriginalLanguageCode,
                    targetLanguageCode,
                    originalText
                )
            }

            val entity = networkTranslate?.asTranslateEntity(
                translateId = UUID.randomUUID().toString(),
                originalText = originalText,
                updateDate = Clock.System.now()
            ) ?: throw Exception("networkTranslate is null")

            translateDao.insertOrUpdateTranslateEntity(entity)
        }

        emitAll(
            translateDao.getTranslateEntityBySrcAndCode(
                originalText,
                realOriginalLanguageCode,
                targetLanguageCode
            ).mapNotNull { it!!.asExternalModel() }
        )
    }

    override fun getTranslates(): Flow<List<Translate>> {
        return translateDao.getTranslateEntities()
            .map { it.map(TranslateEntity::asExternalModel) }
    }

    override fun getTranslates(ids: Set<String>): Flow<List<Translate>> {
        return translateDao.getTranslateEntities(ids)
            .map { it.map(TranslateEntity::asExternalModel) }
    }

    override fun getLatestTranslates(limit: Int): Flow<List<Translate>> {
        return translateDao.getLatestTranslateEntities(limit)
            .map { it.map(TranslateEntity::asExternalModel) }
    }

    override suspend fun deleteTranslate(translateId: String) {
        translateDao.deleteTranslateEntityById(translateId)
    }

    override suspend fun upsertTranslate(translate: Translate) {
        translateDao.insertOrUpdateTranslateEntity(
            translate.asTranslateEntity(Clock.System.now())
        )
    }

    override suspend fun getTranslateFromNetwork(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String
    ): Translate {
        val response = translateApi.translateText(
            originalLanguageCode = originalLanguageCode,
            targetLanguageCode = targetLanguageCode,
            originalText = originalText
        )
        response.errorCode?.let {
            throw Exception("Translation failed with error code: $it")
        }

        val translateId = UUID.randomUUID().toString()

        return response.asExternalModel(id = translateId, originalText = originalText)
    }

}

