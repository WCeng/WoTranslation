package com.wceng.core.testing.repository

import com.wceng.core.testing.data.defaultAutoLanguage
import com.wceng.core.testing.data.languagesTestData
import com.wceng.data.repository.LanguageRepository
import com.wceng.model.Language

class TestLanguageRepository : LanguageRepository {

    override suspend fun getLanguageTextByCode(code: String): String {
        if (code == defaultAutoLanguage.languageCode) {
            return defaultAutoLanguage.languageText
        }

        return getLanguageByCode(code).languageText
    }

    override suspend fun getLanguageByCode(code: String): Language {
        if (code == defaultAutoLanguage.languageCode)
            return defaultAutoLanguage
        return getLanguages().first { it.languageCode == code }
    }

    override suspend fun getLanguages(): List<Language> = languagesTestData
    override suspend fun getLanguages(codes: List<String>): List<Language> {
        return languagesTestData.filter { it.languageCode in codes }
    }

    override suspend fun getAutoLanguage(): Language {
        return defaultAutoLanguage
    }
}


