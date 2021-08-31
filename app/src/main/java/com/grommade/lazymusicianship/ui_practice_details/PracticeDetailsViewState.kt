package com.grommade.lazymusicianship.ui_practice_details

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.entity.StateStudy

@Immutable
data class PracticeDetailsViewState(
    val practiceItem: PracticeWithPieceAndSections = PracticeWithPieceAndSections(),
    val allPieces: List<Piece> = emptyList(),
    val allSections: List<Section> = emptyList(),
    val allStates: List<StateStudy> = emptyList(),
    val errorSections: Boolean = false,
    val saveEnabled: Boolean = false,
) {
    companion object {
        val Empty = PracticeDetailsViewState()
    }
}