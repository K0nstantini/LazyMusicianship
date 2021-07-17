package com.grommade.lazymusicianship.ui_pieces_list

sealed class PiecesListActions {
    object PopulateDB : PiecesListActions()
    object AddNew : PiecesListActions()
    data class Open(val pieceId: Long) : PiecesListActions()
}