package com.grommade.lazymusicianship.ui_statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.domain.observers.ObserveStates
import com.grommade.lazymusicianship.domain.observers.ObserveTimesByPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val observeTimesByPeriod: ObserveTimesByPeriod,
    observeStates: ObserveStates,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatisticsActions>()

    private val filter = MutableStateFlow(StatisticsFilter())

    val state = combine(
        observeTimesByPeriod.observe(),
        observeStates.observe(),
        filter
    ) { times, states, filter ->
        val sumTime = times.sumOf { it.second }.toFloat() / 60

        StatisticsViewState(
            overTimeChartData = times.map {
                it.first to (it.second.toFloat() / 60)
            },
            filter = filter,
            overallTime = sumTime,
            averageTime = sumTime / times.count(),
            allStatesStudy = states
        )
    }

    init {
        observeTimesByPeriod(ObserveTimesByPeriod.Params())
        observeStates(Unit)

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is StatisticsActions.ChangeFilter -> changeFilter(action.filter)
                    else -> {
                    }
                }
            }
        }
    }

    private fun changeFilter(filter: StatisticsFilter) {
        observeTimesByPeriod(ObserveTimesByPeriod.Params(filter.dateStart, filter.dateEnd, filter.timeMode))
        this.filter.value = filter
    }

    fun submitAction(action: StatisticsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}