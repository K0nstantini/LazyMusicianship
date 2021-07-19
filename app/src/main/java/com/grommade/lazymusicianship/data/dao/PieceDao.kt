package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PieceDao : EntityDao<Piece>() {
    @Query("DELETE FROM piece_table")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM piece_table WHERE piece_id = :id")
    abstract suspend fun getPiece(id: Long): Piece?

    @Transaction
    @Query("SELECT * FROM piece_table WHERE piece_id = :id")
    abstract suspend fun getPieceWithSections(id: Long): PieceWithSections?

    @Query("SELECT * FROM piece_table ORDER BY piece_name")
    abstract fun getPiecesFlow(): Flow<List<Piece>>

    @Transaction
    @Query("SELECT * FROM piece_table WHERE piece_id = :id")
    abstract fun getPieceWithSectionsFlow(id: Long): Flow<PieceWithSections?>


}