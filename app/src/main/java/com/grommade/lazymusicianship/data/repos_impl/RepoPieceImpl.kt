package com.grommade.lazymusicianship.data.repos_impl

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.PieceDao
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoPieceImpl @Inject constructor(
    private val pieceDao: PieceDao,
    private val sectionDao: SectionDao,
    private val dispatchers: AppCoroutineDispatchers
) : RepoPiece {

    override suspend fun save(piece: Piece): Long = withContext(dispatchers.io) {
        pieceDao.insertOrUpdate(piece)
    }

    @Transaction
    override suspend fun delete(piece: Piece) = withContext(dispatchers.io) {
        val sections = sectionDao.getSections(piece.id)
        sectionDao.delete(sections)
        pieceDao.delete(piece)
    }

    @Transaction
    override suspend fun deleteAll() = withContext(dispatchers.io) {
        sectionDao.deleteAll()
        pieceDao.deleteAll()
    }

    override suspend fun getPiece(id: Long) = withContext(dispatchers.io) {
        pieceDao.getPiece(id)
    }

    override fun getPiecesFlow() = pieceDao.getPiecesFlow()

}