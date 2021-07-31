package com.grommade.lazymusicianship.ui_statistics

import androidx.compose.runtime.Immutable

@Immutable
data class StatisticsViewState(
    val something: String = ""
) {
    companion object {
        val Empty = StatisticsViewState()
    }
}