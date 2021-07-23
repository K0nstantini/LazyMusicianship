package com.grommade.lazymusicianship.ui_practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.repos.RepoPractice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val repoPractice: RepoPractice
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<PracticeActions>()

    private val practices = repoPractice.practicesItemsFlow
    private val selectedPractice = MutableStateFlow(0L)

    val state = combine(practices, selectedPractice) { practicesList, selected ->
        PracticeViewState(
            practices = practicesList,
            selected = selected
        )
    }

    init {
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

    private fun delete(practice: Practice) {
        viewModelScope.launch {
            repoPractice.delete(practice)
        }
    }

    fun submitAction(action: PracticeActions) {
        viewModelScope.launch { pendingActions.emit(action) }
    }
}