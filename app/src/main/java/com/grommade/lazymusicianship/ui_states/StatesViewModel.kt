package com.grommade.lazymusicianship.ui_states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.observers.ObserveStates
import com.grommade.lazymusicianship.domain.use_cases.DeleteStateStudy
import com.grommade.lazymusicianship.ui.common.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatesViewModel @Inject constructor(
    observeStates: ObserveStates,
    private val snackBarManager: SnackBarManager,
    private val deleteStateStudy: DeleteStateStudy,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StatesActions>()

    private val selectedState = MutableStateFlow(0L)

    val state = combine(observeStates.observe(), selectedState, snackBarManager.errors) { states, selected, error ->
        StatesViewState(
            states = states,
            selected = selected,
            error = error
        )
    }

    init {
        observeStates(Unit)
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is StatesActions.Select -> selectState(action.id)
                    is StatesActions.Delete -> delete(action.state)
                    StatesActions.ClearError -> snackBarManager.removeCurrentError()
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
            val result = deleteStateStudy(DeleteStateStudy.Params(state)).first()
            if (!result) {
                snackBarManager.addError(R.string.snack_state_already_in_use)
            }
        }
    }

    fun submitAction(action: StatesActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}