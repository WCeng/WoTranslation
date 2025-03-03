package com.wceng.network

import com.wceng.network.model.NetworkTranslate

interface WoNetworkDataSource {

    suspend fun translateText(
        originalLanguageCode: String,
        targetLanguageCode: String,
        srcText: String,
    ): NetworkTranslate

}