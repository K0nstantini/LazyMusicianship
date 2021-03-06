package com.grommade.lazymusicianship.data.repos_impl

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.repos.RepoSection
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import com.grommade.lazymusicianship.util.ResourcesHelper
import com.grommade.lazymusicianship.util.ResultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoSectionImpl @Inject constructor(
    private val sectionDao: SectionDao,
    private val practiceDao: PracticeDao,
    private val resHelper: ResourcesHelper,
    private val dispatchers: AppCoroutineDispatchers
) : RepoSection {

    override suspend fun save(section: Section): Long = withContext(dispatchers.io) {
        sectionDao.insertOrUpdate(section.setOrder())
    }

    override fun getSectionsFlow(pieceId: Long) = sectionDao.getSectionsFlow(pieceId)

    @Transaction
    override suspend fun delete(section: Section): ResultOf<Boolean> = withContext(dispatchers.io) {
        val sections = sectionDao.getSections(section.pieceId)
        val sectionsToDel = section.getAllChildren(sections) + section
        return@withContext when (practiceDao.getCountPracticesBySectionsId(sectionsToDel.map { it.id })) {
            0 -> {
                sectionDao.delete(sectionsToDel)
                ResultOf.Success(true)
            }
            else -> ResultOf.Failure(resHelper.errorSectionDel)
        }
    }

    override suspend fun getSection(id: Long) = withContext(dispatchers.io) {
        sectionDao.getSection(id)
    }

    override suspend fun getLastCreated(pieceId: Long) = withContext(dispatchers.io) {
        sectionDao.getLastCreated(pieceId)
    }

    private suspend fun Section.setOrder(): Section {
        if (order != 0) {
            return this
        }
        val lastOrder = sectionDao.getLastOrder(pieceId, parentId) ?: 0
        return copy(order = lastOrder + 1)
    }
}