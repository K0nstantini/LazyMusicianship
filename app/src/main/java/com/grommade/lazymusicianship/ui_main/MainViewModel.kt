package com.grommade.lazymusicianship.ui_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val pendingActions = MutableSharedFlow<MainActions>()

    val state = flow {
        emit(MainViewState())
    }.stateIn(viewModelScope, SharingStarted.Lazily, MainViewState())

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is MainActions.OpenSettings -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun submitAction(action: MainActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}