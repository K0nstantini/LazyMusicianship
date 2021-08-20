package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import java.time.LocalDate

@Immutable
data class PieceWithRecentness(
    @Embedded
    val piece: Piece = Piece(),
    val recentness: LocalDate = LocalDate.MIN,
)