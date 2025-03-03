package com.wceng.core.domain

import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.UserLanguages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserLanguagesUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val languageRepository: LanguageRepository
) {

    operator fun invoke(): Flow<UserLanguages> =
        userDataRepository.userData
            .map { it.languagePreferences}
            .distinctUntilChanged()
            .map { (originalLanguageCode, targetLanguageCode) ->
                val originalLanguage = languageRepository.getLanguageByCode(originalLanguageCode)
                val targetLanguage = languageRepository.getLanguageByCode(targetLanguageCode)
                UserLanguages(
                    originalLanguage = originalLanguage,
                    targetLanguage = targetLanguage
                )
            }
}