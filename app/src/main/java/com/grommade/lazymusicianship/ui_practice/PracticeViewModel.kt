package com.grommade.lazymusicianship.ui_practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.domain.observers.ObservePracticesWithDetails
import com.grommade.lazymusicianship.domain.use_cases.DeletePractice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    observePractices: ObservePracticesWithDetails,
    private val deletePractice: DeletePractice,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PracticeActions>()

    private val selectedPractice = MutableStateFlow(0L)

    val state = combine(
        observePractices.observe(),
        selectedPractice,
    ) { practices, selected ->
        PracticeViewState(
            practices = practices,
            selected = selected,
        )
    }

    init {
        observePractices(Unit)

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is PracticeActions.Select -> selectedPractice.value = action.id
                    is PracticeActions.Delete -> delete(action.practice)
                    else -> {
                    }
                }
            }
        }
    }

    private fun delete(practice: Practice) = viewModelScope.launch {
        deletePractice(DeletePractice.Params(practice)).collect()
    }

    fun submitAction(action: PracticeActions) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}