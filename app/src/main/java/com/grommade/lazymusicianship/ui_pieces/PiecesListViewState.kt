package com.grommade.lazymusicianship.ui_pieces

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithRecentness

@Immutable
data class PiecesListViewState(
    val pieces: List<PieceWithRecentness> = emptyList(),
    val inStudying: List<Piece> = emptyList(),
    val selectedPiece: Long = 0,
    val error: String? = null
) {
    companion object {
        val Empty = PiecesListViewState()
    }
}