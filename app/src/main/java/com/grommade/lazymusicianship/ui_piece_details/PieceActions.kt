package com.grommade.lazymusicianship.ui_piece_details

import com.grommade.lazymusicianship.data.entity.Section

sealed class PieceActions {
    data class ChangeName(val value: String): PieceActions()
    data class ChangeAuthor(val value: String): PieceActions()
    data class ChangeArranger(val value: String): PieceActions()
    data class ChangeTime(val value: Int): PieceActions()
    data class ChangeDescription(val value: String): PieceActions()
    data class OpenSection(val sectionId: Long): PieceActions()
    data class NewSection(val parentId: Long): PieceActions()
    data class SelectSection(val id: Long): PieceActions()
    data class DeleteSection(val section: Section): PieceActions()
    object ClearError : PieceActions()
    object SaveAndClose: PieceActions()
    object Close: PieceActions()
}