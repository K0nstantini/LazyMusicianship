package com.grommade.lazymusicianship.ui_section_details

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Section

@Immutable
data class SectionViewState(
    val section: Section = Section(),
) {
    companion object {
        val Empty = SectionViewState()
    }
}