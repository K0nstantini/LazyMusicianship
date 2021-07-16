package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "section_table")
data class Section(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val name: String = "",
    val piece: Long = 0L,
    val beat: Int = 0,
    val countBars: Int = 0,
): AppEntity {
}