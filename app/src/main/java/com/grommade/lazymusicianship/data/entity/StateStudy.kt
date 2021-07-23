package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "state_study_table")
data class StateStudy(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "state_study_id") override val id: Long = 0L,
    val name: String = "",
    val forPiece: Boolean = true,
    val forSection: Boolean = true,
    val considerTempo: Boolean = false,
    val countNumberOfTimes: Boolean = false,
    val completed: Boolean = false,
) : AppEntity