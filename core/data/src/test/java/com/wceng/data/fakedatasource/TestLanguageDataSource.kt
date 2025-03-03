package com.wceng.data.fakedatasource

import com.wceng.core.data.language.StringResourceDataSource
import com.wceng.core.testing.data.languagesTestData
import com.wceng.model.Language


class TestLanguageDataSource : StringResourceDataSource {
    private val languages = languagesTestData

    override suspend fun getLanguages(): List<Language> = languages
    override suspend fun getLanguages(codes: List<String>): List<Language> {
        return languages.filter { it.languageCode in codes }
    }

    override suspend fun getLanguageTextByCode(code: String): String =
        getLanguageByCode(code).languageText

    override suspend fun getLanguageByCode(code: String): Language =
        languages.first { it.languageCode == code }

    override suspend fun getAutoLanguage(): Language {
        return Language("自动检测", "auto")
    }
}