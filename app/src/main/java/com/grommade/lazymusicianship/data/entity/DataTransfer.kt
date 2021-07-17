package com.grommade.lazymusicianship.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_transfer_table")
data class DataTransfer(
    @PrimaryKey
    override val id: Long = 1,
    @Embedded
    val piece: Piece = Piece(),
    @Embedded
    val section: Section = Section()
) : AppEntity {
}