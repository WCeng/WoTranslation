package com.wceng.network.model

import com.google.gson.annotations.SerializedName
import com.wceng.model.Translate

data class NetworkTranslate (
    @SerializedName("tgt_text")
    val targetText: String,

    @SerializedName("from")
    val originalLanguageCode: String,

    @SerializedName("to")
    val targetLanguageCode: String,

    @SerializedName("error_code")
    val errorCode: Long?
)

fun NetworkTranslate.asExternalModel(
    id: String,
    originalText: String
) = Translate(
    id = id,
    originalLanguageCode = originalLanguageCode,
    targetLanguageCode = targetLanguageCode,
    originalText = originalText,
    targetText = targetText
)
