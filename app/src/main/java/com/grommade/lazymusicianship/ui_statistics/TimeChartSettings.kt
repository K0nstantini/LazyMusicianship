package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class TimeChartSettings(
    val dateStart: LocalDate = LocalDate.MIN,
    val dateEnd: LocalDate = LocalDate.MAX,
    val timeMode: TimeChartMode = TimeChartMode.BY_DAYS,
//    val states: List<StateStudy> = emptyList(),
)