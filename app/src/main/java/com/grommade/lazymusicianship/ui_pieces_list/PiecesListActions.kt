package com.grommade.lazymusicianship.ui_pieces_list

import com.grommade.lazymusicianship.data.entity.Piece

sealed class PiecesListActions {
    object PopulateDB : PiecesListActions()
    object AddNew : PiecesListActions()
    data class SelectPiece(val id: Long): PiecesListActions()
    data class Open(val pieceId: Long) : PiecesListActions()
    data class Delete(val piece: Piece) : PiecesListActions()
}