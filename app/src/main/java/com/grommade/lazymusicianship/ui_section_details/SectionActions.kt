package com.grommade.lazymusicianship.ui_section_details

sealed class SectionActions {
    data class ChangeName(val value: String): SectionActions()
    data class ChangeTempo(val value: Int): SectionActions()
    data class ChangeNew(val value: Boolean): SectionActions()
    data class ChangeDescription(val value: String): SectionActions()
    object SaveAndClose: SectionActions()
    object Close: SectionActions()
}