package com.grommade.lazymusicianship.data.repos

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.PieceDao
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Piece
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoPiece @Inject constructor(
    private val pieceDao: PieceDao,
    private val sectionDao: SectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val piecesFlow = pieceDao.getPiecesFlow()

    suspend fun save(piece: Piece): Long = withContext(ioDispatcher) {
        pieceDao.insertOrUpdate(piece)
    }

    @Transaction
    suspend fun delete(piece: Piece) = withContext(ioDispatcher) {
        val sections = sectionDao.getSections(piece.id)
        sectionDao.delete(sections)
        pieceDao.delete(piece)
    }

    @Transaction
    suspend fun deleteAll() = withContext(ioDispatcher) {
        sectionDao.deleteAll()
        pieceDao.deleteAll()
    }

    suspend fun getPiece(id: Long) = withContext(ioDispatcher) {
        pieceDao.getPiece(id)
    }

}