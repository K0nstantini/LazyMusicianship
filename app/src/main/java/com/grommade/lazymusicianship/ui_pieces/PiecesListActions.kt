package com.grommade.lazymusicianship.ui_pieces

import com.grommade.lazymusicianship.data.entity.Piece

sealed class PiecesListActions {
    object PopulateDB : PiecesListActions()
    object Migration : PiecesListActions()
    object AddNew : PiecesListActions()
    object ClearError : PiecesListActions()
    data class SelectPiece(val id: Long): PiecesListActions()
    data class Open(val pieceId: Long) : PiecesListActions()
    data class Delete(val piece: Piece) : PiecesListActions()
}