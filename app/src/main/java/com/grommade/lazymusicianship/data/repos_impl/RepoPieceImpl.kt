package com.grommade.lazymusicianship.data.repos_impl

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.PieceDao
import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import com.grommade.lazymusicianship.util.ResourcesHelper
import com.grommade.lazymusicianship.util.ResultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoPieceImpl @Inject constructor(
    private val pieceDao: PieceDao,
    private val sectionDao: SectionDao,
    private val practiceDao: PracticeDao,
    private val resHelper: ResourcesHelper,
    private val dispatchers: AppCoroutineDispatchers
) : RepoPiece {

    override suspend fun save(piece: Piece): Long = withContext(dispatchers.io) {
        pieceDao.insertOrUpdate(piece)
    }

    @Transaction
    override suspend fun delete(piece: Piece): ResultOf<Boolean> = withContext(dispatchers.io) {
        return@withContext when (practiceDao.getCountPracticesByPieceId(piece.id)) {
            0 -> {
                val sections = sectionDao.getSections(piece.id)
                sectionDao.delete(sections)
                pieceDao.delete(piece)
                ResultOf.Success(true)
            }
            else -> ResultOf.Failure(resHelper.errorPieceDel)
        }
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

    override fun getPiecesWithRecentnessFlow() = pieceDao.getPiecesWithRecentnessFlow()
}