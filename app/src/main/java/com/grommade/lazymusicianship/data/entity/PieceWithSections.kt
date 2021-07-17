package com.grommade.lazymusicianship.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PieceWithSections(
    @Embedded val piece: Piece,
    @Relation(
        parentColumn = "piece_id",
        entityColumn = "pieceId"
    )
    val sections: List<Section>
) {
}