package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoPractice @Inject constructor(
    private val practiceDao: PracticeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val practicesItemsFlow: Flow<List<PracticeWithPieceAndSections>> = practiceDao.getPracticesItemsFlow()
    val practicesFlow: Flow<List<Practice>> = practiceDao.getPracticesFlow()

    suspend fun save(practice: Practice) = withContext(ioDispatcher) {
        practiceDao.insertOrUpdate(practice)
    }

    suspend fun delete(practice: Practice) = withContext(ioDispatcher) {
        practiceDao.delete(practice)
    }

    suspend fun getPractice(id: Long) = withContext(ioDispatcher) {
        practiceDao.getPractice(id)
    }

    suspend fun getPracticeWithPieceAndSections(id: Long) = withContext(ioDispatcher) {
        practiceDao.getPracticeItem(id)
    }
}