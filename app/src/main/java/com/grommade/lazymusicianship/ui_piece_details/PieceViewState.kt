package com.grommade.lazymusicianship.ui_piece_details

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section

@Immutable
data class PieceViewState(
    val piece: Piece = Piece(),
    val sections: List<Section> = emptyList(),
    val selectedSection: Long = 0,
    val error: String? = null
) {
    companion object {
        val Empty = PieceViewState()
    }
}