package com.wceng.core.testing.data

import com.wceng.model.CollectableTranslate

val collectableTranslatesBooleans = listOf(true, false, true)

val collectableTranslatesTestData = translatesTestData.mapIndexed { index, translate ->
    CollectableTranslate(
        translate = translate,
        collected = collectableTranslatesBooleans[index]
    )
}