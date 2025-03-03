package com.wceng.core.domain

import com.wceng.core.domain.util.sortedByLocalizedName
import com.wceng.core.domain.CollectableTranslatesSortField.NAME
import com.wceng.core.domain.CollectableTranslatesSortField.TRANSLATE_DATE
import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.CollectableTranslate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCollectableTranslatesUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val translateRepository: TranslateRepository,

    ) {
    operator fun invoke(
        sortBy: CollectableTranslatesSortField = TRANSLATE_DATE,
        limit: Int = 50
    ): Flow<List<CollectableTranslate>> =
        userDataRepository.userData.map { it.collectedTranslates }
            .distinctUntilChanged()
            .combine(translateRepository.getLatestTranslates(limit)) { collectedTranslates, translates ->
                translates.map { translate ->
                    CollectableTranslate(
                        translate = translate,
                        collected = translate.id in collectedTranslates
                    )
                }
            }
            .map { latestCollectableTranslates ->
                when (sortBy) {
                    TRANSLATE_DATE -> latestCollectableTranslates
                    NAME -> latestCollectableTranslates.sortedByLocalizedName { it.translate.originalText }
                }
            }
}

enum class CollectableTranslatesSortField {
    TRANSLATE_DATE,
    NAME
}