package com.grommade.lazymusicianship.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PracticeWithDetails(
    @Embedded
    val practice: Practice,

    @Relation(
        parentColumn = "practice_piece_id",
        entityColumn = "piece_id",
        entity = Piece::class
    )
    val pieceWithSections: PieceWithSections,

    @Relation(
        parentColumn = "practice_state_id",
        entityColumn = "state_study_id",
        entity = StateStudy::class
    )
    val stateStudy: StateStudy,

    ) {

    val sectionFrom
        get() = pieceWithSections.sections.find { it.id == practice.sectionIdFrom }

    val sectionTo
        get() = pieceWithSections.sections.find { it.id == practice.sectionIdTo }
}