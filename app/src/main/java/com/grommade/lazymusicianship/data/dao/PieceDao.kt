package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithRecentness
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PieceDao : EntityDao<Piece>() {
    @Query("DELETE FROM piece_table")
    abstract suspend fun deleteAll()

    @Query("SELECT * FROM piece_table WHERE piece_id = :id")
    abstract suspend fun getPiece(id: Long): Piece?

    @Query("SELECT * FROM piece_table ORDER BY piece_name")
    abstract fun getPiecesFlow(): Flow<List<Piece>>

    @Query(
        """
        SELECT piece.*, practice.last_date AS recentness 
        FROM piece_table AS piece
        LEFT JOIN (
            SELECT practice_piece_id AS piece_id, max(date) AS last_date 
            FROM practice_table 
            GROUP BY practice_piece_id
        ) AS practice
        ON piece.piece_id = practice.piece_id
		ORDER BY piece.piece_name
        """
    )
    abstract fun getPiecesWithRecentnessFlow(): Flow<List<PieceWithRecentness>>

}