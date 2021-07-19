package com.grommade.lazymusicianship.ui_piece

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import com.grommade.lazymusicianship.data.entity.Section

@Immutable
data class PieceViewState(
    val piece: Piece = Piece(),
    val sections: List<Section> = emptyList(),
    val selectedSection: Long = 0
) {
    companion object {
        val Empty = PieceViewState()
    }
}