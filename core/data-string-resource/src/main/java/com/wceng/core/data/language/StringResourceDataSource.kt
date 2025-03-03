package com.wceng.core.data.language

import com.wceng.model.Language

interface StringResourceDataSource {

    suspend fun getLanguages(): List<Language>
    suspend fun getLanguageTextByCode(code: String): String
    suspend fun getLanguageByCode(code: String): Language
    suspend fun getLanguages(codes: List<String>): List<Language>
    suspend fun getAutoLanguage(): Language
}