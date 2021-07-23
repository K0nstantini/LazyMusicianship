package com.grommade.lazymusicianship.ui_state_details

sealed class StateDetailsActions {
    data class ChangeName(val value: String): StateDetailsActions()
    data class ChangeForPiece(val value: Boolean): StateDetailsActions()
    data class ChangeForSection(val value: Boolean): StateDetailsActions()
    data class ChangeTempo(val value: Boolean): StateDetailsActions()
    data class ChangeTimes(val value: Boolean): StateDetailsActions()
    data class ChangeCompleted(val value: Boolean): StateDetailsActions()
    object SaveAndClose: StateDetailsActions()
    object Close: StateDetailsActions()
}