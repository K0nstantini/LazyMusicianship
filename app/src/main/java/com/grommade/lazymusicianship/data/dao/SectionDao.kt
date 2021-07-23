package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Section
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SectionDao : EntityDao<Section>() {

    @Query("DELETE FROM section_table  WHERE section_id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM section_table")
    abstract suspend fun deleteAll()

    @Query("DELETE FROM section_table WHERE section_pieceId = :id")
    abstract suspend fun deleteByPieceId(id: Long)

    @Query("SELECT * FROM section_table WHERE section_id = :id")
    abstract suspend fun getSection(id: Long): Section?

    @Query("SELECT * FROM section_table WHERE section_pieceId = :id")
    abstract suspend fun getSectionsByPieceId(id: Long): List<Section>

    @Query("SELECT * FROM section_table WHERE section_pieceId = :id")
    abstract fun getSectionsFlow(id: Long): Flow<List<Section>>

    @Query("SELECT * FROM section_table WHERE section_pieceId = :pieceId ORDER BY section_id DESC LIMIT 1")
    abstract suspend fun getLastCreated(pieceId: Long): Section?

    @Query(lastOrder)
    abstract suspend fun getLastOrder(pieceId: Long, parentId: Long): Int?

    companion object {
        const val lastOrder =
            """
            SELECT `order`
            FROM section_table 
            WHERE section_pieceId = :pieceId AND section_parentId = :parentId
            ORDER BY `order` DESC
            LIMIT 1
            """
    }

}