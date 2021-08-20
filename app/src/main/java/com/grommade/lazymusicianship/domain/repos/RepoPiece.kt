package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithRecentness
import com.grommade.lazymusicianship.util.ResultOf
import kotlinx.coroutines.flow.Flow

interface RepoPiece {
    suspend fun save(piece: Piece): Long
    suspend fun delete(piece: Piece): ResultOf<Boolean>
    suspend fun deleteAll()
    suspend fun getPiece(id: Long): Piece?
    fun getPiecesFlow(): Flow<List<Piece>>
    fun getPiecesWithRecentnessFlow(): Flow<List<PieceWithRecentness>>
}