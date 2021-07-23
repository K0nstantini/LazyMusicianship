package com.grommade.lazymusicianship.data.entity

import androidx.room.Embedded
import androidx.room.Relation

// FIXME: Del?
data class PieceWithSections(
    @Embedded val piece: Piece = Piece(),
    @Relation(
        parentColumn = "piece_id",
        entityColumn = "section_pieceId"
    )
    val sections: List<Section> = emptyList()
) {
}