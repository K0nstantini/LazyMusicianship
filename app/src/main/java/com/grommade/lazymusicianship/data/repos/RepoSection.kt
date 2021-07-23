package com.grommade.lazymusicianship.data.repos

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Section
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoSection @Inject constructor(
    private val sectionDao: SectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getSectionsFlow(pieceId: Long) = sectionDao.getSectionsFlow(pieceId)

    suspend fun save(section: Section): Long = withContext(ioDispatcher) {
        sectionDao.insertOrUpdate(section.setOrder())
    }

    @Transaction
    suspend fun delete(section: Section) = withContext(ioDispatcher) {
        val sections = sectionDao.getSectionsByPieceId(section.pieceId)
        sectionDao.delete(section.getAllChildren(sections))
        sectionDao.delete(section)
    }

    private suspend fun Section.setOrder(): Section {
        if (order != 0) {
            return this
        }
        val lastOrder = sectionDao.getLastOrder(pieceId, parentId) ?: 0
        return copy(order = lastOrder + 1)
    }

    suspend fun getSection(id: Long) = withContext(ioDispatcher) {
        sectionDao.getSection(id)
    }

    suspend fun getSectionsByPieceId(id: Long) = withContext(ioDispatcher) {
        sectionDao.getSectionsByPieceId(id)
    }

    suspend fun getLastCreated(pieceId: Long) = withContext(ioDispatcher) {
        sectionDao.getLastCreated(pieceId)
    }
}