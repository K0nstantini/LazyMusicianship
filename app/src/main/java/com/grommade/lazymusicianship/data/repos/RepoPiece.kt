package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.PieceDao
import com.grommade.lazymusicianship.data.entity.Piece
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoPiece @Inject constructor(
    private val pieceDao: PieceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val piecesFlow = pieceDao.getPiecesFlow()

    suspend fun save(piece: Piece): Long = withContext(ioDispatcher) {
        pieceDao.insertOrUpdate(piece)
    }

    suspend fun deleteAll() = withContext(ioDispatcher) {
        pieceDao.deleteAll()
    }

    suspend fun getPiece(id: Long) = withContext(ioDispatcher) {
        pieceDao.getPiece(id)
    }

    suspend fun getPieceWithSections(id: Long) = withContext(ioDispatcher) {
        pieceDao.getPieceWithSections(id)
    }
}