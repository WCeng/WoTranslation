package com.wceng.core.data.language

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wceng.core.data.string.resource.R
import com.wceng.model.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LanguageStringResourceTest {

    private lateinit var languageDataSource: LanguageStringResource
    private lateinit var context: Context
    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        languageDataSource = LanguageStringResource(context, dispatcher)
    }

    @Test
    fun getLanguages() = testScope.runTest {
        assertEquals(true, languageDataSource.getLanguages().isNotEmpty())
    }

    @Test
    fun getLanguageTextByCode() = testScope.runTest {
        val languageText = languageDataSource.getLanguageTextByCode("en")

        assertEquals(englishLanguage().languageText, languageText)
    }

    @Test
    fun getLanguageTextByCode_whenGetAuto_returnAutoText()  = testScope.runTest {
        val languageText = languageDataSource.getLanguageTextByCode("auto")
        assertEquals("自动检测"  , languageText)
    }

    @Test
    fun getLanguageByCode() = testScope.runTest {
        val loaded = languageDataSource.getLanguageByCode("en")

        assertEquals(englishLanguage(), loaded)
    }

    @Test
    fun getLanguageByCode_codeIsAuto_returnAutoLanguage() = testScope.runTest {
        val loaded = languageDataSource.getLanguageByCode("auto")

        assertEquals(Language(
            "自动检测",
            "auto"
        ), loaded)
    }

    @Test
    fun getLanguagesByCodes() = testScope.runTest {
        assertEquals(
            listOf(englishLanguage()),
            languageDataSource.getLanguages(listOf("en"))
        )

        assertEquals(
            listOf(englishLanguage(), chineseLanguage()),
            languageDataSource.getLanguages(listOf("en", "zh"))
        )
    }

    @Test
    fun getAutoLanguageByCode() = testScope.runTest {
        assertEquals(
            languageDataSource.getAutoLanguage(),
            languageDataSource.getLanguageByCode("auto")
        )
    }

    @Test
    fun getAutoLanguageTextByCode() = testScope.runTest {
        assertEquals(
            languageDataSource.getAutoLanguage().languageText,
            languageDataSource.getLanguageTextByCode("auto")
        )
    }

    private fun englishLanguage()  = Language(
        "英语",
        "en"
    )
    private fun chineseLanguage() =Language(
        "中文（简体）",
        "zh"
    )

}