package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "piece_table")
data class Piece(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "piece_id")
    override val id: Long = 0,
    @ColumnInfo(name = "piece_name")
    val name: String = "",
    val author: String = "",
    val arranger: String = "",
    @ColumnInfo(name = "piece_firstTime")
    val firstTime: Boolean = true,
    @ColumnInfo(name = "piece_tempo")
    val tempo: Int = 0,
    val time: Int = 0,
    val description: String = ""
) : AppEntity {
}