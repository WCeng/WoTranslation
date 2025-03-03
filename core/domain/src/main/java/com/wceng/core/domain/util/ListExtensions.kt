package com.wceng.core.domain.util

import java.text.Collator
import java.util.Locale

fun <T> List<T>.sortedByLocalizedName(
    locale: Locale = Locale.getDefault(),
    strength: Int = Collator.PRIMARY,
    selector: (T) -> String
): List<T> {
    val collator = Collator.getInstance(locale).apply {
        this.strength = strength
    }
    return this.sortedWith { a, b ->
        collator.compare(selector(a), selector(b))
    }
}