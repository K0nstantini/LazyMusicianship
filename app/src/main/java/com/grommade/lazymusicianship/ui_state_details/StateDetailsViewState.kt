package com.grommade.lazymusicianship.ui_state_details

import androidx.compose.runtime.Immutable
import androidx.core.content.pm.ShortcutInfoCompatSaver
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class StateDetailsViewState(
    val stateStudy: StateStudy = StateStudy(),
) {
    companion object {
        val Empty = StateDetailsViewState()
    }
}