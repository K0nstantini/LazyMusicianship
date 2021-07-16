package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "state_study_piece_table")
data class StateStudyPiece(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    override val name: String = "",
    override val considerBeat: Boolean = false,
    override val considerTimes: Boolean = false,
): StateStudy(), AppEntity {
}