package com.grommade.lazymusicianship.use_cases

import com.grommade.lazymusicianship.ui.components.timepicker.yearMonth
import com.grommade.lazymusicianship.util.extentions.sameWeek
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class TimeByPeriodsTest {

    /** ==================================== byWeeks ===================================== */
    @Test
    fun byWeeks_isCorrect() {
        var result = true

        val dates = mutableListOf<Pair<LocalDate, Float>>()
        for (i in 0..15) {
            dates.add(LocalDate.now().minusDays(0).plusDays(i.toLong()) to 1f)
        }
        dates.byWeeks().forEach {
            println(it)
        }

        Assert.assertEquals(true, result)
    }

    private fun List<Pair<LocalDate, Float>>.byWeeks(): List<Pair<LocalDate, Float>> {
        var lastDate: LocalDate? = null
        return map { pair ->
            val date = when (lastDate?.sameWeek(pair.first)) {
                null, false -> pair.first
                true -> lastDate ?: pair.first
            }
            lastDate = date
            date to pair.second
        }
            .groupBy { it.first }
            .map { keyValue ->
                keyValue.key to keyValue.value.map { it.second }.toFloatArray().sum()
            }
    }

    /** ==================================== byMonths ===================================== */

    @Test
    fun byMonths_isCorrect() {
        var result = true

        val dates = mutableListOf<Pair<LocalDate, Int>>()
        for (i in 0..85) {
            dates.add(LocalDate.now().minusDays(0).plusDays(i.toLong()) to 1)
        }
        dates.byMonths().forEach {
            println(it)
        }

        Assert.assertEquals(true, result)
    }

    private fun List<Pair<LocalDate, Int>>.byMonths(): List<Pair<LocalDate, Int>> {
        return groupBy { it.first.yearMonth }.values
            .map { value -> value.first().first to value.sumOf { it.second } }
    }

}