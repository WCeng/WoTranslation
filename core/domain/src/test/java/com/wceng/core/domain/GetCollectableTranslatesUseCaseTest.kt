package com.wceng.core.domain

import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.model.CollectableTranslate
import com.wceng.model.Translate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCollectableTranslatesUseCaseTest() {

    private val userDataRepository = TestUserDataRepository()
    private val translateRepository = TestTranslateRepository()

    val useCase = GetCollectableTranslatesUseCase(
        userDataRepository = userDataRepository,
        translateRepository = translateRepository
    )

    @Test
    fun getCollectableTranslatesUseCase() = runTest {
        val collectedTranslates = useCase()

        translateRepository.sendTranslates(testTranslates)
        userDataRepository.apply {
            setCollectTranslate(testTranslates[0].id, true)
            setCollectTranslate(testTranslates[2].id, true)
        }


        assertEquals(
            setOf(
                CollectableTranslate(testTranslates[0], true),
                CollectableTranslate(testTranslates[1], false),
                CollectableTranslate(testTranslates[2], true),
            ),
            collectedTranslates.first().toSet()
        )
    }

    private val testTranslates = listOf(
        Translate(
            id = "1",
            originalLanguageCode = "en",
            targetLanguageCode = "zh",
            originalText = "Hello",
            targetText = "你好"
        ),
        Translate(
            id = "2",
            originalLanguageCode = "en",
            targetLanguageCode = "zh",
            originalText = "Hello",
            targetText = "你好"
        ),
        Translate(
            id = "3",
            originalLanguageCode = "en",
            targetLanguageCode = "zh",
            originalText = "Hello",
            targetText = "你好"
        )
    )

}