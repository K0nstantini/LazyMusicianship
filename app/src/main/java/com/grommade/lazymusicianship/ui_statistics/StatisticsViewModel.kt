package com.grommade.lazymusicianship.ui_statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor() : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatisticsActions>()

    val state = flow {
        emit(StatisticsViewState())
    }.stateIn(viewModelScope, SharingStarted.Lazily, StatisticsViewState())

    init {
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