package com.wceng.core.domain

import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentLanguagesUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val languageRepository: LanguageRepository
) {

    operator fun invoke(isSelectOriginalLanguage: Boolean): Flow<List<Language>> {
        return userDataRepository.userData
            .map {
                if (isSelectOriginalLanguage)
                    it.recentOriginalLanguageCodes
                else
                    it.recentTargetLanguageCodes
            }
            .map { languageCodes ->
                languageRepository.getLanguages(languageCodes)
            }
    }

}