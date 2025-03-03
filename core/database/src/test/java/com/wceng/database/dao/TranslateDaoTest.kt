package com.wceng.database.dao

import com.wceng.database.DatabaseTest
import com.wceng.database.model.TranslateEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TranslateDaoTest : DatabaseTest() {

    @Test
    fun getTranslates() = runTest {
        insertTranslates()

        val loaded = translateDao.getTranslateEntities().first()

        assertEquals(3, loaded.size)
    }

    @Test
    fun insertTranslate_isReturnedCorrectly() = runTest {
        insertTranslates()
        translateDao.insertOrUpdateTranslateEntity(testTranslateEntity("4", "Hello4"))

        val savedTranslates = translateDao.getTranslateEntities().first()

        assertEquals(listOf("1", "2", "3", "4"), savedTranslates.map { it.id })
    }

    @Test
    fun insertTranslate_replaceWhenExist() = runTest {
        insertTranslates()
        translateDao.insertOrUpdateTranslateEntity(testTranslateEntity("1", "newHello"))

        val savedTranslates = translateDao.getTranslateEntities().first()

        assertEquals("newHello", savedTranslates.first().originalText)
        assertEquals(3, savedTranslates.size)
    }

    @Test
    fun deleteTranslate() = runTest {
        insertTranslates()
        translateDao.deleteTranslateEntityById("3")

        val savedTranslates = translateDao.getTranslateEntities().first()

        assertEquals(listOf("1", "2"), savedTranslates.map { it.id })
    }

    @Test
    fun getTranslateEntityBySrcAndCode_whenHasMatchingData_isExistsAndReturned() = runTest {
        insertTranslates()
        val first = testTranslateEntity(
            id = "4",
            originalText = "Hello4",
            originalLanguageCode = "en",
            targetLanguageCode = "zh"
        )

        translateDao.insertOrUpdateTranslateEntity(first)

        assertTrue(
            translateDao.isTranslateEntityExists("Hello4", "en", "zh")
                .first()
        )

        val loaded = translateDao.getTranslateEntityBySrcAndCode(
            "Hello4", "en", "zh"
        )
            .first()


        assertEquals(first.id, loaded!!.id)
    }

    @Test
    fun getTranslateEntityBySrcAndCode_notExistWhenGet() = runTest {
        insertTranslates()

        val loaded = translateDao.getTranslateEntityBySrcAndCode(
            "Hello4", "en", "zh"
        )
            .first()

        assertEquals(null, loaded)
    }

    @Test
    fun getTranslateEntitiesByIds() = runTest {
        insertTranslates()

        assertEquals(
            listOf<String>(),
            translateDao.getTranslateEntities(setOf()).first().map { it.id }
        )

        assertEquals(
            listOf("1", "2"),
            translateDao.getTranslateEntities(setOf("1", "2")).first().map { it.id }
        )
    }

    @Test
    fun getLatestTranslateEntities() = runTest {
        insertTranslates()

        assertEquals(
            listOf("3"),
            translateDao.getLatestTranslateEntities(limit = 1).first().map { it.id }
        )

        assertEquals(
            listOf("3", "2"),
            translateDao.getLatestTranslateEntities(limit = 2).first().map { it.id }
        )

        translateDao.insertOrUpdateTranslateEntity(
            translateDao.getTranslateEntityById("2").first().copy(
                originalText = "newHello",
                translatedDate = Clock.System.now()
            ),
        )

        assertEquals(
            listOf("2", "3"),
            translateDao.getLatestTranslateEntities(limit = 2).first().map { it.id }
        )

    }

    @Test
    fun isTranslateEntityExists() = runTest {
        insertTranslates()

        val newTranslateEntity = TranslateEntity(
            id = "4",
            originalLanguageCode = "en",
            targetLanguageCode = "zh",
            originalText = "d",
            targetText = "a",
            translatedDate = Clock.System.now()
        )

        val first = translateDao.isTranslateEntityExists(
            originalText = newTranslateEntity.originalText,
            originalLanguageCode = newTranslateEntity.originalLanguageCode,
            targetLanguageCode = newTranslateEntity.targetLanguageCode
        ).first()

        assertFalse(first)

        translateDao.insertOrUpdateTranslateEntity(newTranslateEntity)

        val second = translateDao.isTranslateEntityExists(
            originalText = newTranslateEntity.originalText,
            originalLanguageCode = newTranslateEntity.originalLanguageCode,
            targetLanguageCode = newTranslateEntity.targetLanguageCode
        ).first()

        assertTrue(second)
    }

    private suspend fun insertTranslates() {
        translateDao.apply {
            insertOrUpdateTranslateEntity(testTranslateEntity("1", "Hello1"))
            insertOrUpdateTranslateEntity(testTranslateEntity("2", "Hello2"))
            insertOrUpdateTranslateEntity(testTranslateEntity("3", "Hello3"))
        }
    }
}

private fun testTranslateEntity(
    id: String = "0",
    originalText: String,
    originalLanguageCode: String = "",
    targetLanguageCode: String = ""
) = TranslateEntity(
    id = id,
    originalText = originalText,
    targetText = "",
    originalLanguageCode = originalLanguageCode,
    targetLanguageCode = targetLanguageCode,
    translatedDate = Clock.System.now()
)