package com.grommade.lazymusicianship.ui_statistics

sealed class StatisticsActions {
    data class ChangeFilter(val filter: StatisticsFilter) : StatisticsActions()
}