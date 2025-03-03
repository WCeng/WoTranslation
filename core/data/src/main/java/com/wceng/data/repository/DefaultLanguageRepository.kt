package com.wceng.data.repository

import com.wceng.core.data.language.StringResourceDataSource
import com.wceng.model.Language
import javax.inject.Inject

class DefaultLanguageRepository
@Inject constructor(
    private val languageDataSource: StringResourceDataSource
) : LanguageRepository {

    override suspend fun getLanguages(): List<Language> {
        return languageDataSource.getLanguages()
    }

    override suspend fun getLanguages(codes: List<String>): List<Language> {
        return languageDataSource.getLanguages(codes)
    }

    override suspend fun getAutoLanguage(): Language {
        return languageDataSource.getAutoLanguage()
    }

    override suspend fun getLanguageTextByCode(code: String): String {
        return languageDataSource.getLanguageTextByCode(code)
    }

    override suspend fun getLanguageByCode(code: String): Language {
        return languageDataSource.getLanguageByCode(code)
    }
}