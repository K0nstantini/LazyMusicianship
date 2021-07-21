package com.grommade.lazymusicianship.ui_state_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.data.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StateDetailsViewModel @Inject constructor(
    private val repoStateStudy: RepoStateStudy,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<StateDetailsActions>()

    private val currentState = MutableStateFlow(StateStudy())

    val state = currentState.map { stateStudy ->
        StateDetailsViewState(stateStudy = stateStudy)
    }

    // FIXME
    val navigateToBack = MutableSharedFlow<Boolean>()

    init {
        viewModelScope.launch {
            initStateStudy()
            pendingActions.collect { action ->
                when (action) {
                    is StateDetailsActions.ChangeName -> changeState { copy(name = action.value) }
                    is StateDetailsActions.ChangeForPiece -> changeState { copy(forPiece = action.value) }
                    is StateDetailsActions.ChangeForSection -> changeState { copy(forSection = action.value) }
                    is StateDetailsActions.ChangeTempo -> changeState { copy(considerTempo = action.value) }
                    is StateDetailsActions.ChangeTimes -> changeState { copy(countNumberOfTimes = action.value) }
                    is StateDetailsActions.ChangeCompleted -> changeState { copy(completed = action.value) }
                    StateDetailsActions.Save -> save()
                    else -> {
                    }
                }
            }
        }
    }

    private suspend fun initStateStudy() {
        val stateId = handle.get<Long>(Keys.STATE_ID) ?: 0
        repoStateStudy.getState(stateId)?.let { stateStudy ->
            currentState.value = stateStudy
        }
    }

    private fun changeState(foo: StateStudy.() -> StateStudy) {
        currentState.value = foo(currentState.value)
    }

    private fun save() {
        viewModelScope.launch {
            repoStateStudy.save(currentState.value)
            navigateToBack.emit(true)
        }
    }


    fun submitAction(action: StateDetailsActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}