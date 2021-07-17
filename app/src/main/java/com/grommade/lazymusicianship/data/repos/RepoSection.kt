package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.SectionDao
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoSection @Inject constructor(
    private val sectionDao: SectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun save(section: Section): Long = withContext(ioDispatcher) {
        sectionDao.insertOrUpdate(section)
    }

    suspend fun getSection(id: Long) = withContext(ioDispatcher) {
        sectionDao.getSection(id)
    }
}