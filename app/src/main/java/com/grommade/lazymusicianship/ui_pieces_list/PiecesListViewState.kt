package com.grommade.lazymusicianship.ui_pieces_list

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece

@Immutable
data class PiecesListViewState(
    val pieces: List<Piece> = emptyList()
) {
    companion object {
        val Empty = PiecesListViewState()
    }
}