@file:OptIn(ExperimentalCoroutinesApi::class)

package com.wceng.core.domain

import com.wceng.data.repository.LanguageRepository
import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.UserTranslate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserTranslateUseCase @Inject constructor(
    private val getUserLanguagesUseCase: GetUserLanguagesUseCase,
    private val translateRepository: TranslateRepository,
    private val userDataRepository: UserDataRepository,
    private val languageRepository: LanguageRepository
) {

    operator fun invoke(originalText: String): Flow<UserTranslate> =
        combine(
            getUserLanguagesUseCase(),
            userDataRepository.userData
                .map { it.collectedTranslates }
                .distinctUntilChanged(),
            ::Pair
        ).flatMapLatest { (userLanguages, collectedTranslates) ->
            val originalLanguageCode = userLanguages.originalLanguage.languageCode
            val originalLanguageText = userLanguages.originalLanguage.languageText
            val targetLanguageCode = userLanguages.targetLanguage.languageCode
            val targetLanguageText = userLanguages.targetLanguage.languageText

            val isOriginalLanguageAutoDetect =
                languageRepository.getAutoLanguage().languageCode == originalLanguageCode



            translateRepository.getTranslate(
                originalLanguageCode, targetLanguageCode, originalText, isOriginalLanguageAutoDetect
            )
                .map {
                    UserTranslate(
                        translate = it,
                        originalLanguageText = if (isOriginalLanguageAutoDetect)
                            languageRepository.getLanguageTextByCode(it.originalLanguageCode)
                        else
                            originalLanguageText,
                        targetLanguageText = targetLanguageText,
                        collected = it.id in collectedTranslates
                    )
                }
        }
}