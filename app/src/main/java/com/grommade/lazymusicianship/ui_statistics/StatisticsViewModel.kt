package com.grommade.lazymusicianship.ui_statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.domain.observers.ObserveTimesByDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    observeTimesByDays: ObserveTimesByDays
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatisticsActions>()

    val state = observeTimesByDays.observe().map { times ->
        StatisticsViewState(
            timesByDays = times.mapIndexed { index, pair ->
                Pair((index + 1).toString(), pair.second.toFloat() / 60)
            }
        )
    }

    init {
        observeTimesByDays(ObserveTimesByDays.Params())

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    else -> {
                    }
                }
            }
        }
    }

    fun submitAction(action: StatisticsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}