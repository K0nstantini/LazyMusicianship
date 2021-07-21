package com.grommade.lazymusicianship.ui_states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.data.repos.RepoStateStudy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatesViewModel @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatesActions>()

    private val statesStudy = repoStateStudy.statesFlow
    private val selectedState = MutableStateFlow(0L)

    val state = combine(statesStudy, selectedState) { state, selected ->
        StatesViewState(
            states = state,
            selected = selected
        )
    }

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is StatesActions.Select -> selectState(action.id)
                    is StatesActions.Delete -> delete(action.state)
                    else -> {
                    }
                }
            }
        }
    }

    private fun selectState(id: Long) {
        selectedState.value = id
    }

    private fun delete(state: StateStudy) {
        viewModelScope.launch {
            repoStateStudy.delete(state)
        }
    }

    fun submitAction(action: StatesActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}