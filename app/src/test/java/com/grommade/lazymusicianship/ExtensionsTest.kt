package com.grommade.lazymusicianship

import com.grommade.lazymusicianship.util.extentions.diffDays
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import kotlin.math.abs

class ExtensionsTest {

    @Test
    fun sameWeek_isCorrect() {
        var result = true

        val today = LocalDate.now()
        result = result && today.sameWeek(today)
        result = result && today.sameWeek(today.plusDays(1))
        result = result && today.sameWeek(today.plusDays(2))
        result = result && today.sameWeek(today.plusDays(5))
        result = result && today.sameWeek(today.minusDays(1))
        result = result && today.minusDays(1).sameWeek(today)
        result = result && today.minusDays(1).sameWeek(today.plusDays(5))
        result = result && today.plusDays(5).sameWeek(today.minusDays(1))

        result = result && !today.sameWeek(today.plusDays(6))
        result = result && !today.sameWeek(today.plusDays(9))
        result = result && !today.sameWeek(today.minusDays(2))
        result = result && !today.sameWeek(today.minusDays(20))
        result = result && !today.plusDays(6).sameWeek(today.minusDays(1))
        result = result && !today.plusDays(5).sameWeek(today.minusDays(2))

        Assert.assertEquals(true, result)
    }

    private fun LocalDate.sameWeek(date: LocalDate) = when {
        this > date -> dayOfWeek.ordinal > date.dayOfWeek.ordinal
        this < date -> dayOfWeek.ordinal < date.dayOfWeek.ordinal
        else -> true
    } && abs(diffDays(date)) < 7

}