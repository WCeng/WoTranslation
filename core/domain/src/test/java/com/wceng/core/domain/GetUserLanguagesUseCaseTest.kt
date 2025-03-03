package com.wceng.core.domain

import com.wceng.core.testing.data.defaultChineseLanguage
import com.wceng.core.testing.data.defaultEnglishLanguage
import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.model.UserLanguages
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetUserLanguagesUseCaseTest() {

    private val userDataRepository = TestUserDataRepository()
    private val languageRepository = TestLanguageRepository()

    private val useCase = GetUserLanguagesUseCase(
        userDataRepository = userDataRepository,
        languageRepository = languageRepository
    )

    @Test
    fun getUserLanguagesUseCase() = runTest {
        val userLanguages = useCase()

        userDataRepository.setUserData(emptyUserData)

        assertEquals(
            UserLanguages(
                originalLanguage = defaultEnglishLanguage,
                targetLanguage = defaultChineseLanguage
            ),
            userLanguages.first()
        )
    }
}