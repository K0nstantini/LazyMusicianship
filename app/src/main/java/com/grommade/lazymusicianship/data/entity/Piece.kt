package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "piece_table")
data class Piece(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val title: String = "",
    val author: String = "",
    val arranger: String = "",
    val beat: Int = 0,
    val description: String = ""
) : AppEntity {
}