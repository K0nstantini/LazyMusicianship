package com.grommade.lazymusicianship.ui_practice

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections

@Immutable
data class PracticeViewState(
    val practices: List<PracticeWithPieceAndSections> = emptyList(),
    val selected: Long = 0
) {
    companion object {
        val Empty = PracticeViewState()
    }
}