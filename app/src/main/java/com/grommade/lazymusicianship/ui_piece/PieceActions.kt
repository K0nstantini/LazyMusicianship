package com.grommade.lazymusicianship.ui_piece

sealed class PieceActions {
    data class ChangeName(val value: String): PieceActions()
    data class ChangeAuthor(val value: String): PieceActions()
    data class ChangeArranger(val value: String): PieceActions()
    data class OpenSection(val order: Int): PieceActions()
    data class NewSection(val pieceId: Long): PieceActions()
    data class SelectSection(val order: Int): PieceActions()
    data class DeleteSection(val order: Int): PieceActions()
    object Save: PieceActions()
    object Close: PieceActions()
}