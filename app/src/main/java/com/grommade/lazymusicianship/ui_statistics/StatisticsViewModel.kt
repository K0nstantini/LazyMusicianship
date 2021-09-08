package com.grommade.lazymusicianship.ui_statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.domain.observers.ObserveStates
import com.grommade.lazymusicianship.domain.observers.ObserveTimesByPeriod
import com.grommade.lazymusicianship.domain.use_cases.GetTimeChartSettings
import com.grommade.lazymusicianship.domain.use_cases.SaveTimeChartSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val observeTimesByPeriod: ObserveTimesByPeriod,
    private val timeChartSettings: GetTimeChartSettings,
    private val saveTimeChartSettings: SaveTimeChartSettings,
    private val observeStates: ObserveStates,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatisticsActions>()

    private val filter = MutableStateFlow(TimeChartSettings())

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
        initData()

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

    private fun initData() = viewModelScope.launch {
        timeChartSettings(Unit).first().let {
            filter.value = it
            observeTimesByPeriod(ObserveTimesByPeriod.Params(it))
        }
        observeStates(Unit)
    }

    private fun changeFilter(newFilter: TimeChartSettings) {
        observeTimesByPeriod(ObserveTimesByPeriod.Params(newFilter))
        filter.value = newFilter
        saveFilter()
    }

    private fun saveFilter() = viewModelScope.launch {
        saveTimeChartSettings(SaveTimeChartSettings.Params(filter.value)).collect()
    }

    fun submitAction(action: StatisticsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}