package com.wceng.data.repository

import com.wceng.model.Language

interface LanguageRepository {

    suspend fun getLanguageTextByCode(code: String): String
    suspend fun getLanguageByCode(code: String): Language
    suspend fun getLanguages(): List<Language>
    suspend fun getLanguages(codes: List<String>): List<Language>
    suspend fun getAutoLanguage(): Language
}