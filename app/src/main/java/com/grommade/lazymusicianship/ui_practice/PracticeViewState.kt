package com.grommade.lazymusicianship.ui_practice

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PracticeWithDetails

@Immutable
data class PracticeViewState(
    val practices: List<PracticeWithDetails> = emptyList(),
    val allPieces: List<Piece> = emptyList(),
    val selected: Long = 0
) {
    companion object {
        val Empty = PracticeViewState()
    }
}