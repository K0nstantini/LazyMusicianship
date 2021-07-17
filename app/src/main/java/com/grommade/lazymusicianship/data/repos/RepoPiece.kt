package com.grommade.lazymusicianship.data.repos

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.PieceDao
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
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
    suspend fun save(pieceWithSections: PieceWithSections) = withContext(ioDispatcher) {
        val id = pieceDao.insertOrUpdate(pieceWithSections.piece)
        pieceWithSections.sections.forEach { section ->
            sectionDao.insertOrUpdate(section.copy(pieceId = id))
        }

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