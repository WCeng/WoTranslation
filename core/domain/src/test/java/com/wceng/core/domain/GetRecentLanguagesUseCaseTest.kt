package com.wceng.core.domain

import com.wceng.core.testing.data.defaultChineseLanguage
import com.wceng.core.testing.data.defaultEnglishLanguage
import com.wceng.core.testing.repository.TestLanguageRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.model.Language
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRecentLanguagesUseCaseTest {

    private val userDataRepository = TestUserDataRepository()

    private val languageRepository = TestLanguageRepository()

    private val getRecentLanguagesUseCase = GetRecentLanguagesUseCase(
        userDataRepository = userDataRepository,
        languageRepository = languageRepository
    )

    @Test
    fun whenHasUserRecentOriginalLanguageCodes_validateExpectedValue() = runTest {
        userDataRepository.setUserData(
            emptyUserData.copy(
                recentOriginalLanguageCodes = listOf("zh")
            )
        )

        val loaded = getRecentLanguagesUseCase(isSelectOriginalLanguage = true)
            .first()

        assertEquals(listOf(defaultChineseLanguage), loaded)
        assertEquals(listOf<Language>(), getRecentLanguagesUseCase(false).first())
    }

    @Test
    fun whenHasUserRecentTargetLanguageCodes_validateExpectedValue() = runTest {
        userDataRepository.setUserData(
            emptyUserData.copy(
                recentTargetLanguageCodes = listOf("en")
            )
        )

        val loaded = getRecentLanguagesUseCase(isSelectOriginalLanguage = false)
            .first()

        assertEquals(listOf(defaultEnglishLanguage), loaded)
        assertEquals(listOf<Language>(), getRecentLanguagesUseCase(true).first())
    }
}