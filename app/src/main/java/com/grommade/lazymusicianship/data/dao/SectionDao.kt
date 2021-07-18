package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Section

@Dao
abstract class SectionDao : EntityDao<Section>() {

    @Query("DELETE FROM section_table  WHERE section_id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM section_table WHERE pieceId = :id")
    abstract suspend fun deleteByPieceId(id: Long)

    @Query("SELECT * FROM section_table WHERE section_id = :id")
    abstract suspend fun getSection(id: Long): Section?

    @Query("SELECT * FROM section_table WHERE pieceId = :id")
    abstract suspend fun getSectionsByPieceId(id: Long): List<Section>
}