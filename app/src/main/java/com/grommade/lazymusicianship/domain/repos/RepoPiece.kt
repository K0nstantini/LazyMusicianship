package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.entity.Piece
import kotlinx.coroutines.flow.Flow

interface RepoPiece {
    suspend fun save(piece: Piece): Long
    suspend fun delete(piece: Piece)
    suspend fun deleteAll()
    suspend fun getPiece(id: Long): Piece?
    fun getPiecesFlow(): Flow<List<Piece>>
}