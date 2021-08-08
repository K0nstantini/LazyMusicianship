package com.grommade.lazymusicianship.ui_statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.domain.observers.ObserveStates
import com.grommade.lazymusicianship.domain.observers.ObserveTimesByDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val observeTimesByDays: ObserveTimesByDays,
    private val observeStates: ObserveStates,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatisticsActions>()

    private val lineChartFilter = MutableStateFlow(StatisticsFilter())

    val state = combine(
        observeTimesByDays.observe(),
        observeStates.observe(),
        lineChartFilter
    ) { times, states, filter ->
        StatisticsViewState(
            chartValues = times.mapIndexed { index, pair ->
                Pair((index + 1).toString(), pair.second.toFloat() / 60)
            },
            filter = filter,
            allStatesStudy = states
        )
    }

    init {
        observeTimesByDays(ObserveTimesByDays.Params())
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
        lineChartFilter.value = filter
        observeTimesByDays(ObserveTimesByDays.Params(filter.dateStart, filter.dateEnd))
    }


    fun submitAction(action: StatisticsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}