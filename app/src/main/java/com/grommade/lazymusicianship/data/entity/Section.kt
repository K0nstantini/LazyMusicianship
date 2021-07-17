package com.grommade.lazymusicianship.data.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
@Entity(tableName = "section_table")
data class Section(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "section_id")
    override val id: Long = 0,
    @ColumnInfo(name = "section_name")
    val name: String = "",
    val pieceId: Long = 0L,
    val order: Int = -1,
    val isNew: Boolean = true,
    @ColumnInfo(name = "section_beat")
    val beat: Int = 0,
    val countBars: Int = 0,
): AppEntity, Parcelable {
}