package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Immutable
@Entity(tableName = "practice_table")
data class Practice(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val pieceId: Long = 0L,
    val sectionId: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val elapsedTime: Int = 0,
    val stateId: Long = 0L,
): AppEntity {
}