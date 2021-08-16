package com.grommade.lazymusicianship.ui_statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.domain.observers.ObserveStates
import com.grommade.lazymusicianship.domain.observers.ObserveTimesByDays
import com.grommade.lazymusicianship.util.extentions.sameMonth
import com.grommade.lazymusicianship.util.extentions.stringMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val observeTimesByDays: ObserveTimesByDays,
    observeStates: ObserveStates,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatisticsActions>()

    private val lineChartFilter = MutableStateFlow(StatisticsFilter())

    private val timeMode = MutableStateFlow(TimeChartMode.BY_DAYS)

    val state = combine(
        observeTimesByDays.observe(),
        observeStates.observe(),
        timeMode,
        lineChartFilter
    ) { times, states, mode, filter ->
        StatisticsViewState(
//            overTimeChartData = times.map {
//                it.first to (it.second.toFloat() / 60)
//            },
            // fixme
            overTimeChartData = (0..99).map {
                LocalDate.now().plusDays(it.toLong()) to (0..4).random().toFloat()
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

    private fun getLabelX(
        mode: TimeChartMode,
        lastDate: LocalDate?,
        nextDate: LocalDate?,
        date: LocalDate,
    ): String {
        if (mode == TimeChartMode.BY_DAYS) {
            if (nextDate != null && (lastDate == null || !lastDate.sameMonth(date))) {
                return date.stringMonth()
            }
        }
        return ""
    }

    private fun changeFilter(filter: StatisticsFilter) {
        lineChartFilter.value = filter
        observeTimesByDays(ObserveTimesByDays.Params(filter.dateStart, filter.dateEnd))
    }


    fun submitAction(action: StatisticsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}