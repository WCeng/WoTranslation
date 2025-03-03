package com.wceng.data.repository

import com.wceng.core.testing.data.defaultEnglishLanguage
import com.wceng.data.fakedatasource.TestLanguageDataSource
import com.wceng.datastore.defaultOriginalLanguageCode
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultLanguageRepositoryTest {

    private lateinit var subject: DefaultLanguageRepository
    private lateinit var languageDataSource: TestLanguageDataSource

    @Before
    fun setup() {
        languageDataSource = TestLanguageDataSource()
        subject = DefaultLanguageRepository(languageDataSource = languageDataSource)
    }

    @Test
    fun getLanguages() = runTest {
        assertEquals(languageDataSource.getLanguages(), subject.getLanguages())
    }

    @Test
    fun getLanguageTextByCode() = runTest {
        val loaded = subject.getLanguageTextByCode(defaultOriginalLanguageCode)

        assertEquals(languageDataSource.getLanguageTextByCode(defaultOriginalLanguageCode), loaded)
    }

    @Test
    fun getLanguageByCode() = runTest {
        val loaded = subject.getLanguageByCode(defaultOriginalLanguageCode)

        assertEquals(languageDataSource.getLanguageByCode(defaultOriginalLanguageCode), loaded)
    }

    @Test
    fun getLanguagesByCodes() = runTest {
        assertEquals(
            listOf(defaultEnglishLanguage),
            subject.getLanguages(listOf("en"))
        )

        assertEquals(
            listOf(defaultEnglishLanguage),
            languageDataSource.getLanguages(listOf("en"))
        )
    }


}