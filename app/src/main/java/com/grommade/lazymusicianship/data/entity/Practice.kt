package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Immutable
@Entity(tableName = "practice_table")
data class Practice(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "practice_id") override val id: Long = 0,
    @ColumnInfo(name = "practice_piece_id") val pieceId: Long = 0L,
    @ColumnInfo(name = "practice_section_id_from") val sectionIdFrom: Long = 0L,
    @ColumnInfo(name = "practice_section_id_to") val sectionIdTo: Long = 0L,
    @ColumnInfo(name = "practice_state_id") val stateId: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val elapsedTime: Int = 0,
    @ColumnInfo(name = "practice_tempo") val tempo: Int = 0,
    val countTimes: Int = 0,
) : AppEntity