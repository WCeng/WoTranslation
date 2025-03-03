package com.wceng.core.data.test.repository

import com.wceng.core.data.language.LanguageStringResource
import com.wceng.data.repository.LanguageRepository
import com.wceng.model.Language
import javax.inject.Inject

class FakeLanguageRepository @Inject constructor(
    private val languageStringResource: LanguageStringResource
) : LanguageRepository {
    override suspend fun getLanguageTextByCode(code: String): String {
        return languageStringResource.getLanguageTextByCode(code)
    }

    override suspend fun getLanguageByCode(code: String): Language {
        return languageStringResource.getLanguageByCode(code)
    }

    override suspend fun getLanguages(): List<Language> {
        return languageStringResource.getLanguages()
    }

    override suspend fun getLanguages(codes: List<String>): List<Language> {
        return languageStringResource.getLanguages(codes)
    }

    override suspend fun getAutoLanguage(): Language {
        return languageStringResource.getAutoLanguage()
    }
}