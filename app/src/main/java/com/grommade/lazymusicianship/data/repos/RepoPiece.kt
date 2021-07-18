package com.grommade.lazymusicianship.data.repos

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.PieceDao
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.PieceWithSections
import com.grommade.lazymusicianship.data.entity.Section
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
        deleteOldSections(pieceWithSections.sections, id)
        saveActualSections(pieceWithSections.sections, id)
    }

    private suspend fun deleteOldSections(sections: List<Section>, id: Long) = withContext(ioDispatcher) {
        sectionDao.getSectionsByPieceId(id)
            .map { it.id }
            .minus(sections.map { it.id })
            .forEach {
                sectionDao.delete(it)
            }
    }

    private suspend fun saveActualSections(sections: List<Section>, id: Long) = withContext(ioDispatcher) {
        sections
            .sortedBy { it.order }
            .forEachIndexed { index, section ->
                sectionDao.insertOrUpdate(section.copy(order = index, pieceId = id))
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