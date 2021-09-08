package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.ui.components.timepicker.yearMonth
import com.grommade.lazymusicianship.ui_statistics.TimeChartMode
import com.grommade.lazymusicianship.ui_statistics.TimeChartSettings
import com.grommade.lazymusicianship.util.extentions.sameWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ObserveTimesByPeriod @Inject constructor(
    private val repoPractice: RepoPractice
) : ObserveUserCase<ObserveTimesByPeriod.Params, List<Pair<LocalDate, Int>>>() {

    override fun createObservable(params: Params): Flow<List<Pair<LocalDate, Int>>> {
        return repoPractice.getTimesByDaysFlow(params.filter.dateStart, params.filter.dateEnd).map { values ->
            val startDate = getDate(values, params.filter.dateStart)
            val endDate = getDate(values, params.filter.dateEnd)
            val rangeDates = if (startDate == null || endDate == null) {
                emptyList()
            } else {
                rangeDates(startDate, endDate)
            }
            rangeDates.map { date ->
                val time = values.find { it.date == date }?.time ?: 0
                Pair(date, time)
            }.changeByPeriod(params.filter.timeMode)
        }
    }

    private fun List<Pair<LocalDate, Int>>.changeByPeriod(mode: TimeChartMode): List<Pair<LocalDate, Int>> {
        return when (mode) {
            TimeChartMode.BY_DAYS -> this
            TimeChartMode.BY_WEEKS -> byWeeks()
            TimeChartMode.BY_MONTHS -> byMonths()
            TimeChartMode.BY_YEARS -> byYears()
        }
    }

    private fun List<Pair<LocalDate, Int>>.byWeeks(): List<Pair<LocalDate, Int>> {
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
            .map { keyValue -> keyValue.key to keyValue.value.sumOf { it.second } }
    }

    private fun List<Pair<LocalDate, Int>>.byMonths() =
        groupBy { it.first.yearMonth }.values
            .map { value -> value.first().first to value.sumOf { it.second } }

    private fun List<Pair<LocalDate, Int>>.byYears() =
        groupBy { it.first.year }.values
            .map { value -> value.first().first to value.sumOf { it.second } }

    private fun rangeDates(startDate: LocalDate, endDate: LocalDate): List<LocalDate> = when {
        startDate > endDate -> listOf()
        startDate == endDate -> listOf(startDate)
        else -> listOf(startDate) + rangeDates(startDate.plusDays(1), endDate)
    }

    private fun getDate(values: List<PracticeDao.TimesByDays>, date: LocalDate) = when (date) {
        LocalDate.MIN -> values.firstOrNull()?.date
        LocalDate.MAX -> LocalDate.now()
        else -> date
    }

    data class Params(val filter: TimeChartSettings)
}