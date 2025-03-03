@file:OptIn(ExperimentalCoroutinesApi::class)

package com.wceng.core.domain

import com.wceng.data.repository.TranslateRepository
import com.wceng.data.repository.UserDataRepository
import com.wceng.model.CollectableTranslate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCollectedTranslatesUseCase @Inject constructor(
    private val translateRepository: TranslateRepository,
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(): Flow<List<CollectableTranslate>> {
        return userDataRepository.userData
            .map { it.collectedTranslates }
            .flatMapLatest { collectedTranslates ->
                translateRepository.getTranslates(collectedTranslates)
                    .map { translates ->
                        translates.map {
                            CollectableTranslate(
                                translate = it,
                                collected = true
                            )
                        }
                    }
            }
    }

}