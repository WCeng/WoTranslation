package com.wceng.data.repository

import com.wceng.data.fakedatasource.TestTranslateApi
import com.wceng.data.fakedatasource.TestTranslateDao
import com.wceng.database.model.TranslateEntity
import com.wceng.database.model.asExternalModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DefaultTranslateRepositoryTest {

    private lateinit var subject: DefaultTranslateRepository
    private lateinit var translateDao: TestTranslateDao
    private lateinit var translateApi: TestTranslateApi

    private val translateEntity1 = TranslateEntity(
        id = "1",
        originalLanguageCode = "zh",
        targetLanguageCode = "en",
        originalText = "你好",
        targetText = "Hello",
        translatedDate = Clock.System.now()
    )

    @Before
    fun setUp() {
        translateDao = TestTranslateDao()
        translateApi = TestTranslateApi()
        subject = DefaultTranslateRepository(
            translateDao = translateDao,
            translateApi = translateApi
        )
    }

    @Test
    fun getTranslateById_insertTranslate_getById() = runTest {
        translateDao.apply {
            insertOrUpdateTranslateEntity(translateEntity1)
        }

        assertEquals(translateEntity1.asExternalModel(), subject.getTranslate("1").first())
    }

    @Test
    fun getTranslateCached_whenFetchFromNetworkAndNotCachedInLocal_isCachedInLocal() = runTest {
        subject.getTranslate(
            originalLanguageCode = "en",
            targetLanguageCode = "zh",
            originalText = "你好"
        ).first()

        assertEquals(1, translateDao.getTranslateEntities().first().size)
    }

    @Test
    fun getTranslateCached_whenHasCacheInLocal_isLoadedFromLocal() = runTest {
        translateDao.insertOrUpdateTranslateEntity(translateEntity1)

        translateApi.allowParamsException = true

        val load = subject.getTranslate(
            originalLanguageCode = translateEntity1.originalLanguageCode,
            targetLanguageCode = translateEntity1.targetLanguageCode,
            originalText = translateEntity1.originalText
        ).first()

        assertEquals(translateEntity1.asExternalModel(), load)
    }

    @Test
    fun getTranslate_whenOriginalLanguageIsAutoDetect_and_noCachingInLocal_thenIsCachedInLocal_and_apiInvokeCountIsOne() =
        runTest {
            subject.getTranslate(
                originalLanguageCode = translateEntity1.originalLanguageCode,
                targetLanguageCode = translateEntity1.targetLanguageCode,
                originalText = translateEntity1.originalText,
                isOriginalLanguageAutoDetect = true
            ).first()

            assertEquals(
                1,
                translateDao.getTranslateEntities().first().size
            )

            assertEquals(1, translateApi.invokeSuccessfullyCount)
        }

    @Test
    fun getTranslate_whenOriginalLanguageIsAutoDetect_and_hasDataCached_thenApiInvokeCountIsOne() =
        runTest {
            translateDao.insertOrUpdateTranslateEntity(translateEntity1)

            translateApi.returnedOriginalLanguageCode = translateEntity1.originalLanguageCode

            subject.getTranslate(
                originalLanguageCode = translateEntity1.originalLanguageCode,
                targetLanguageCode = translateEntity1.targetLanguageCode,
                originalText = translateEntity1.originalText,
                isOriginalLanguageAutoDetect = true
            ).first()

            assertEquals(1, translateApi.invokeSuccessfullyCount)
        }


    @Test(expected = Exception::class)
    fun getTranslate_NetworkError_ifThrowException() = runTest {
        translateApi.allowNetworkException = true

        subject.getTranslate(
            originalLanguageCode = translateEntity1.originalLanguageCode,
            targetLanguageCode = translateEntity1.targetLanguageCode,
            originalText = translateEntity1.originalText
        ).first()
    }

    @Test(expected = Exception::class)
    fun getTranslate_paramsInCorrect_ifThrowException() = runTest {
        translateApi.allowParamsException = true

        subject.getTranslate(
            originalLanguageCode = translateEntity1.originalLanguageCode,
            targetLanguageCode = translateEntity1.targetLanguageCode,
            originalText = translateEntity1.originalText
        ).first()
    }

    @Test
    fun getTranslates() = runTest {
        translateDao.insertOrUpdateTranslateEntity(translateEntity1)

        assertEquals(1, subject.getTranslates().first().size)
        assertEquals(
            translateEntity1.asExternalModel(),
            subject.getTranslates().first()[0]
        )
    }

    @Test
    fun deleteTranslate() = runTest {
        translateDao.insertOrUpdateTranslateEntity(translateEntity1)
        translateDao.insertOrUpdateTranslateEntity(
            translateEntity1.copy(id = "2")
        )

        subject.deleteTranslate("1")

        assertEquals(1, subject.getTranslates().first().size)
        assertEquals(
            translateEntity1.copy("2").asExternalModel(),
            subject.getTranslates().first()[0]
        )
    }

    @Test
    fun insertTranslate_replace() = runTest {
        translateDao.insertOrUpdateTranslateEntity(translateEntity1)

        subject.upsertTranslate(
            translateEntity1.copy(originalLanguageCode = "aa").asExternalModel()
        )

        assertEquals(1, translateDao.getTranslateEntities().first().size)
        assertEquals("aa", translateDao.getTranslateEntities().first()[0].originalLanguageCode)
    }

    @Test
    fun getLatestTranslatesWithLimit() = runTest {
        subject.apply {
            upsertTranslate(testTranslateEntities[0].asExternalModel())
            upsertTranslate(testTranslateEntities[1].asExternalModel())
            upsertTranslate(testTranslateEntities[2].asExternalModel())
        }

        assertEquals(
            listOf("3", "2"),
            subject.getLatestTranslates(2).first().map { it.id }
        )

        subject.upsertTranslate(
            subject.getTranslate("2").first().copy(originalText = "a")
        )

        assertEquals(
            listOf("2", "3"),
            subject.getLatestTranslates(2).first().map { it.id }
        )
    }

    @Test
    fun getTranslatesByIds() = runTest {
        subject.apply {
            upsertTranslate(testTranslateEntities[0].asExternalModel())
            upsertTranslate(testTranslateEntities[1].asExternalModel())
            upsertTranslate(testTranslateEntities[2].asExternalModel())
        }

        assertTrue(
            subject.getTranslates(setOf("1", "2")).first().map { it.id }
                .containsAll(listOf("1", "2"))
        )
    }

}

private val testTranslateEntities = listOf(
    TranslateEntity(
        id = "1",
        originalLanguageCode = "",
        targetLanguageCode = "",
        originalText = "",
        targetText = "",
        translatedDate = Clock.System.now()
    ),
    TranslateEntity(
        id = "2",
        originalLanguageCode = "",
        targetLanguageCode = "",
        originalText = "",
        targetText = "",
        translatedDate = Clock.System.now()
    ),
    TranslateEntity(
        id = "3",
        originalLanguageCode = "",
        targetLanguageCode = "",
        originalText = "",
        targetText = "",
        translatedDate = Clock.System.now()
    ),
)