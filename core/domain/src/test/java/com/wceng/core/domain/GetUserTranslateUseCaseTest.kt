package com.wceng.core.domain

import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.model.Translate
import com.wceng.model.UserTranslate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserTranslateUseCaseTest {

    private val userDataRepository = TestUserDataRepository()
    private val languageRepository = TestLanguageRepository()
    private val translateRepository = TestTranslateRepository()

    private val userLanguagesUseCase = GetUserLanguagesUseCase(
        userDataRepository = userDataRepository,
        languageRepository = languageRepository
    )

    private val useCase = GetUserTranslateUseCase(
        getUserLanguagesUseCase = userLanguagesUseCase,
        translateRepository = translateRepository,
        userDataRepository = userDataRepository,
        languageRepository = languageRepository,
    )

    @Test
    fun getUserTranslate_collectTranslateToggle() = runTest {
        val userTranslate = useCase(originalText = "Hello")

        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(listOf(testTranslate))

        assertEquals(
            testUserTranslate,
            userTranslate.first()
        )

        userDataRepository.setCollectTranslate("1", true)

        assertEquals(
            testUserTranslate.copy(collected = true),
            userTranslate.first()
        )
    }

    private val testTranslate = Translate(
        id = "1",
        originalLanguageCode = "en",
        targetLanguageCode = "zh",
        originalText = "Hello",
        targetText = "你好"
    )

    private val testUserTranslate =
        UserTranslate(
            translate = testTranslate,
            originalLanguageText = "英文",
            targetLanguageText = "中文（简体）",
            collected = false,
        )


}