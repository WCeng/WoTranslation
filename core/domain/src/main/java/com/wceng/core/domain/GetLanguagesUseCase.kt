package com.wceng.core.domain

import com.wceng.core.domain.LanguageSortField.NONE
import com.wceng.core.domain.LanguageSortField.NAME
import com.wceng.core.domain.util.sortedByLocalizedName
import com.wceng.data.repository.LanguageRepository
import com.wceng.model.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLanguagesUseCase @Inject constructor(
    private val languageRepository: LanguageRepository,
) {
    operator fun invoke(
        sortBy: LanguageSortField = NONE
    ): Flow<List<Language>> {
        return flow {
            val languages = languageRepository.getLanguages()

            val sortedLanguages = when (sortBy) {
                NAME -> languages.sortedByLocalizedName { it.languageText }
                else -> languages
            }

            emit(
                value = sortedLanguages
            )
        }
    }
}

enum class LanguageSortField {
    NONE,
    NAME
}

