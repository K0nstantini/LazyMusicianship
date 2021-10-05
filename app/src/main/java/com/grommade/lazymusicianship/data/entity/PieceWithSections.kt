package com.grommade.lazymusicianship.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PieceWithSections(
    @Embedded val piece: Piece = Piece(),
    @Relation(
        parentColumn = "piece_id",
        entityColumn = "section_piece_id",
    )
    val sections: List<Section> = emptyList()
)