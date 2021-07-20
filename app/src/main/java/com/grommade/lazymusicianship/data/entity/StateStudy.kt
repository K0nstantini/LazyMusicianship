package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "state_study_table")
data class StateStudy(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0L,
    val name: String = "",
    val forPiece: Boolean = true,
    val forSection: Boolean = true,
    val considerBeat: Boolean = false,
    val considerTimes: Boolean = false,
    val completed: Boolean = false,
) : AppEntity