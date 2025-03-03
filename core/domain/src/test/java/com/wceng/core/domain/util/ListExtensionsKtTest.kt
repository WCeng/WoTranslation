package com.wceng.core.domain.util

import org.junit.Assert.*
import org.junit.Test

class ListExtensionsKtTest(){
    @Test
    fun isSortedByEnglishName() {
        val first = listOf("b", "a", "c")

        val second = first.sortedByLocalizedName { it }

        assertEquals(
            listOf("a", "b", "c"),
            second
        )
    }

    @Test
    fun isSortedByChineseName() {
        val first = listOf("杨", "爱", "莫")

        val second = first.sortedByLocalizedName { it }

        assertEquals(
            listOf("爱", "莫", "杨"),
            second
        )
    }

}