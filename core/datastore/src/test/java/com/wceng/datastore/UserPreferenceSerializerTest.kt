package com.wceng.datastore

import androidx.datastore.core.CorruptionException
import com.wceng.core.datastore.userPreference
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserPreferenceSerializerTest(){

    private val userPreferencesSerializer = UserPreferenceSerializer()


    @Test
    fun defaultUserPreferences_isEmpty() {
        assertEquals(
            userPreference {
                // Default value
            },
            userPreferencesSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingUserPreferences_outputsCorrectValue() = runTest {
        val expectedUserPreferences = userPreference {
           shouldHideOnboarding = true
        }

        val outputStream = ByteArrayOutputStream()

        expectedUserPreferences.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val actualUserPreferences = userPreferencesSerializer.readFrom(inputStream)

        assertEquals(
            expectedUserPreferences,
            actualUserPreferences,
        )
    }


    @Test(expected = CorruptionException::class)
    fun readingInvalidUserPreferences_throwsCorruptionException() = runTest {
        userPreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }

}