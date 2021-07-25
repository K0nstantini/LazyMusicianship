package com.grommade.lazymusicianship.ui_states

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class StatesViewState(
    val states: List<StateStudy> = emptyList(),
    val selected: Long = 0,
    @StringRes val error: Int? = null
) {
    companion object {
        val Empty = StatesViewState()
    }
}