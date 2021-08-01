package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.runtime.Immutable

@Immutable
data class StatisticsViewState(
    val timesByDays: List<Pair<String, Float>> = emptyList()
) {
    companion object {
        val Empty = StatisticsViewState()
    }
}