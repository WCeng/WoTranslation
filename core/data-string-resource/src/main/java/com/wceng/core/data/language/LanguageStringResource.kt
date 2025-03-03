package com.wceng.core.data.language

import android.content.Context
import com.wceng.common.di.IoDispatcher
import com.wceng.core.data.string.resource.R
import com.wceng.model.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LanguageStringResource
@Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : StringResourceDataSource {

    private fun getLanguagesInternal(): List<Language> {
        val codes = context.resources.getStringArray(R.array.language_codes)
        val languageTexts = context.resources.getStringArray(R.array.languages)
        assert(codes.size == languageTexts.size)
        return languageTexts.zip(codes, ::Language)
    }

    override suspend fun getLanguages(): List<Language> {
        return withContext(dispatcher) {
            getLanguagesInternal()
        }
    }

    override suspend fun getLanguages(codes: List<String>): List<Language> {
        return withContext(dispatcher) {
            codes.mapNotNull {  code->
                getLanguages().associateBy { it.languageCode }[code]
            }
        }
    }

    override suspend fun getLanguageTextByCode(code: String): String {
        if (code == getAutoLanguage().languageCode) return getAutoLanguage().languageText

        return withContext(dispatcher) {
            getLanguagesInternal().find { it.languageCode == code }?.languageText
                ?: throw IllegalArgumentException("Language code: $code not found")
        }
    }

    override suspend fun getLanguageByCode(code: String): Language {
        if (code == getAutoLanguage().languageCode) return getAutoLanguage()

        return withContext(dispatcher) {
            getLanguagesInternal().find { it.languageCode == code }
                ?: throw IllegalArgumentException("Language code: $code not found")
        }
    }

    override suspend fun getAutoLanguage(): Language {
        return withContext(dispatcher) {
            Language(
                languageText = context.resources.getString(R.string.resource_auto_language_text),
                languageCode = context.resources.getString(R.string.resource_auto_language_code),
            )
        }
    }
}