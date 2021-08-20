package com.grommade.lazymusicianship.ui_state_details

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class StateDetailsViewState(
    val stateStudy: StateStudy = StateStudy(),
) {
    companion object {
        val Empty = StateDetailsViewState()
    }
}