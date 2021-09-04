package com.grommade.lazymusicianship.data.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
@Entity(tableName = "section_table")
data class Section(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "section_id") override val id: Long = 0L,
    @ColumnInfo(name = "section_name") val name: String = "",
    @ColumnInfo(name = "section_piece_id") val pieceId: Long = 0L,
    @ColumnInfo(name = "section_parent_id") val parentId: Long = 0L,
    val order: Int = 0,
    @ColumnInfo(name = "section_first_time") val firstTime: Boolean = true,
    @ColumnInfo(name = "section_tempo") val tempo: Int = 0,
    @ColumnInfo(name = "section_description") val description: String = "",
) : AppEntity, Parcelable {

    val isChild: Boolean
        get() = parentId > 0

    fun getLevel(sections: List<Section>) = generateSequence(this) { section ->
        sections.find { it.id == section.parentId }
    }.count() - 1

    fun getAllChildren(sections: List<Section>, allChildren: MutableList<Section> = mutableListOf()): List<Section> {
        val children = sections.filter { it.parentId == id }
        allChildren.addAll(children)
        children.forEach {
            it.getAllChildren(sections, allChildren)
        }
        return allChildren
    }

    fun hierarchicalSort(sections: List<Section>): String {
        return generateSequence(this) { section ->
            sections.find { it.id == section.parentId }
        }.toList().reversed().joinToString { "${it.order}:" + it.id }
    }

}