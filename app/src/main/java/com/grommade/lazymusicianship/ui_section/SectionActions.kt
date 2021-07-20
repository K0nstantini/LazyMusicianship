package com.grommade.lazymusicianship.ui_section

sealed class SectionActions {
    data class ChangeName(val value: String): SectionActions()
    data class ChangeTempo(val value: String): SectionActions()
    data class ChangeNew(val value: Boolean): SectionActions()
    object Save: SectionActions()
    object Close: SectionActions()
}