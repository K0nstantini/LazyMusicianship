package com.grommade.lazymusicianship.ui_piece

sealed class PieceActions {
    object Save: PieceActions()
    object Close: PieceActions()
}