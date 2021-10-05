package com.grommade.lazymusicianship

import org.junit.Assert
import org.junit.Test

class ConvertersTest {

    @Test
    fun fromListLong_isCorrect() {
        val value1 = fromListLong(listOf(1, 6, 2555))
        val value2 = fromListLong(emptyList())
        Assert.assertEquals("1,6,2555", value1)
        Assert.assertEquals("", value2)
    }

    private fun fromListLong(value: List<Long>): String = value.joinToString(",")

    @Test
    fun toListLong_isCorrect() {
        val original = listOf<Long>(1, 6, 2555)

        val value1 = toListLong("1,6,2555")
        val value2 = toListLong("")
        Assert.assertEquals(true, value1.containsAll(original) && original.containsAll(value1))
        Assert.assertEquals(emptyList<Long>(), value2)
    }

    private fun toListLong(value: String): List<Long> = when (value) {
        "" -> emptyList()
        else -> value.split(",").map { it.toLong() }
    }

}