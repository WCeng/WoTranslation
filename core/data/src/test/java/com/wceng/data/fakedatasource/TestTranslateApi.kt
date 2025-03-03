package com.wceng.data.fakedatasource

import com.wceng.network.api.TranslateApi
import com.wceng.network.model.NetworkTranslate

class TestTranslateApi : TranslateApi {

    var allowParamsException = false

    var allowNetworkException = false

    var invokeSuccessfullyCount = 0

//    var targetText: String = ""
    var returnedOriginalLanguageCode: String? = null
//    var targetLanguageCode: String = ""

    override suspend fun translateText(
        originalLanguageCode: String,
        targetLanguageCode: String,
        originalText: String
    ): NetworkTranslate {
        if (allowParamsException) {
            return NetworkTranslate(
                targetText = "",
                originalLanguageCode = "",
                targetLanguageCode = "",
                errorCode = 11111
            )
        } else if (allowNetworkException) {
            throw Exception("Test except")
        } else {
            invokeSuccessfullyCount++
            return NetworkTranslate(
                targetText = "Target",
                originalLanguageCode =  originalLanguageCode,
                targetLanguageCode = targetLanguageCode,
                errorCode = null
            )
        }
    }
}