package com.wceng.network.api

import com.wceng.core.network.BuildConfig
import com.wceng.network.model.NetworkTranslate
import retrofit2.http.GET
import retrofit2.http.Query


interface TranslateApi {

    @GET(value = "translation?apikey=${BuildConfig.TRANSLATE_API_KEY}")
    suspend fun translateText(
        @Query("from") originalLanguageCode: String,
        @Query("to") targetLanguageCode: String,
        @Query("src_text") originalText: String,
    ): NetworkTranslate

}