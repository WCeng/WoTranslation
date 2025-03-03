package com.wceng.core.domain

import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.Translate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTranslateNoCached @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val translateRepository: TranslateRepository
) {

    operator fun invoke(originalText: String): Flow<Translate> {
        return userDataRepository.userData.map { it.languagePreferences }
            .map { languagePreferences ->
                val originalLanguageCode = languagePreferences.originalLanguageCode
                val targetLanguageCode = languagePreferences.targetLanguageCode
                translateRepository.getTranslateFromNetwork(
                    originalLanguageCode = originalLanguageCode,
                    targetLanguageCode = targetLanguageCode,
                    originalText = originalText
                )
            }
    }
}