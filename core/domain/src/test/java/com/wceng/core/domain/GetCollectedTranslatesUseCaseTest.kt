package com.wceng.core.domain

import com.wceng.core.testing.data.translatesTestData
import com.wceng.core.testing.repository.TestTranslateRepository
import com.wceng.core.testing.repository.TestUserDataRepository
import com.wceng.core.testing.repository.emptyUserData
import com.wceng.model.CollectableTranslate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCollectedTranslatesUseCaseTest() {
    private val userDataRepository = TestUserDataRepository()
    private val translateRepository = TestTranslateRepository()

    private val getCollectedTranslatesUseCase = GetCollectedTranslatesUseCase(
        userDataRepository = userDataRepository,
        translateRepository = translateRepository
    )

    @Test
    fun whenNoCollectedTranslates_isReturnEmptyList() = runTest {
        userDataRepository.setUserData(emptyUserData)
        translateRepository.sendTranslates(translatesTestData)

        assert(getCollectedTranslatesUseCase().first().isEmpty())
    }

    @Test
    fun whenHasCollectedTranslates_isReturnCorrectedList() = runTest {
        userDataRepository.setUserData(
            emptyUserData.copy(
                collectedTranslates = setOf(
                    translatesTestData[0].id,
                    translatesTestData[2].id
                )
            )
        )

        translateRepository.sendTranslates(translatesTestData)

        assertEquals(
            listOf(
                CollectableTranslate(
                    translate = translatesTestData[0],
                    collected = true
                ),
                CollectableTranslate(
                    translate = translatesTestData[2],
                    collected = true
                ),
            ),
            getCollectedTranslatesUseCase().first()
        )
    }
}