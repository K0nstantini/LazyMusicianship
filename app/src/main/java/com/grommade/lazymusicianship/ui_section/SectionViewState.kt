package com.grommade.lazymusicianship.ui_section

import androidx.compose.runtime.Immutable
import androidx.core.content.pm.ShortcutInfoCompatSaver
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import com.grommade.lazymusicianship.data.entity.Section

@Immutable
data class SectionViewState(
    val section: Section = Section(),
) {
    companion object {
        val Empty = SectionViewState()
    }
}