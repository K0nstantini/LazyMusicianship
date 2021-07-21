package com.grommade.lazymusicianship.ui_main

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece

@Immutable
data class MainViewState(
    val pieces: List<Piece> = emptyList(),
    val selectedPiece: Long = 0
) {
    companion object {
        val Empty = MainViewState()
    }
}