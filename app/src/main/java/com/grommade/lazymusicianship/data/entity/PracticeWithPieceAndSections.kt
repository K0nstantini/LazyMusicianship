package com.grommade.lazymusicianship.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PracticeWithPieceAndSections(
    @Embedded
    val practice: Practice = Practice(),

    @Relation(parentColumn = "practice_pieceId", entityColumn = "piece_id", entity = Piece::class)
    val piece: Piece = Piece(),

    @Relation(parentColumn = "practice_state_id", entityColumn = "state_study_id", entity = StateStudy::class)
    val stateStudy: StateStudy = StateStudy(),

    @Relation(parentColumn = "practice_section_id_from", entityColumn = "section_id", entity = Section::class)
    val sectionFrom: Section? = null,

    @Relation(parentColumn = "practice_section_id_to", entityColumn = "section_id", entity = Section::class)
    val sectionTo: Section? = null,
)