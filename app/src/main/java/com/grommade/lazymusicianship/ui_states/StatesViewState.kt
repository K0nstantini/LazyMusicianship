package com.grommade.lazymusicianship.ui_states

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class StatesViewState(
    val states: List<StateStudy> = emptyList(),
    val selected: Long = 0
) {
    companion object {
        val Empty = StatesViewState()
    }
}