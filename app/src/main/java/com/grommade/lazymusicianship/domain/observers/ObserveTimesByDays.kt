package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ObserveTimesByDays @Inject constructor(
    private val repoPractice: RepoPractice
) : ObserveUserCase<ObserveTimesByDays.Params, List<Pair<LocalDate, Int>>>() {

    override fun createObservable(params: Params): Flow<List<Pair<LocalDate, Int>>> {
        return repoPractice.getTimesByDaysFlow(params.startDate, params.endDate).map { values ->
            val startDate = getDate(values.firstOrNull(), params.startDate)
            val endDate = getDate(values.lastOrNull(), params.endDate)
            val rangeDates = if (startDate == null || endDate == null) {
                emptyList()
            } else {
                rangeDates(startDate, endDate)
            }
            rangeDates.map { date ->
                val time = values.find { it.date == date }?.time ?: 0
                Pair(date, time)
            }
        }
    }

    private fun rangeDates(startDate: LocalDate, endDate: LocalDate): List<LocalDate> = when {
        startDate > endDate -> listOf()
        startDate == endDate -> listOf(startDate)
        else -> listOf(startDate) + rangeDates(startDate.plusDays(1), endDate)
    }

    private fun getDate(timesByDays: PracticeDao.TimesByDays?, date: LocalDate) = when {
        date.isNotEmpty() -> date
        timesByDays != null -> timesByDays.date
        else -> null
    }

    private fun LocalDate.isNotEmpty() = this > LocalDate.MIN && this < LocalDate.MAX

    data class Params(
        val startDate: LocalDate = LocalDate.MIN,
        val endDate: LocalDate = LocalDate.MAX
    )
}