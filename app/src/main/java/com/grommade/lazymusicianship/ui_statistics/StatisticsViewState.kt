package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.StateStudy
import java.time.LocalDate

@Immutable
data class StatisticsViewState(
    val overTimeChartData: List<Pair<LocalDate, Float>> = emptyList(),
    val filter: StatisticsFilter = StatisticsFilter(),
    val overallTime: Float = 0f,
    val averageTime: Float = 0f,
    val allStatesStudy: List<StateStudy> = emptyList()
) {
    companion object {
        val Empty = StatisticsViewState()
    }
}