package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.StateStudy
import java.time.LocalDate

@Immutable
data class StatisticsFilter(
    val dateStart: LocalDate = LocalDate.MIN,
    val dateEnd: LocalDate = LocalDate.MAX,
    val timeMode: TimeChartMode = TimeChartMode.BY_DAYS,
    val states: List<StateStudy> = emptyList(),
)