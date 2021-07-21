package com.grommade.lazymusicianship.ui_main

import androidx.compose.runtime.Immutable
import com.grommade.lazymusicianship.data.entity.Piece

@Immutable
data class MainViewState(
    val something: String = ""
) {
    companion object {
        val Empty = MainViewState()
    }
}