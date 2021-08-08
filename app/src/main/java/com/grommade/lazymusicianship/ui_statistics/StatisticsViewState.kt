package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class StatisticsViewState(
    val chartValues: List<Pair<String, Float>> = emptyList(),
    val filter: StatisticsFilter = StatisticsFilter(),
    val allStatesStudy: List<StateStudy> = emptyList()
) {
    companion object {
        val Empty = StatisticsViewState()
    }
}