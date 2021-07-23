package com.grommade.lazymusicianship.ui_practice

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class PracticeViewState(
    val practices: List<PracticeWithPieceAndSections> = emptyList(),
    val selected: Long = 0
) {
    companion object {
        val Empty = PracticeViewState()
    }
}