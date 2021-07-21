package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.StateStudyDao
import com.grommade.lazymusicianship.data.entity.StateStudy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoStateStudy @Inject constructor(
    private val stateStudyDao: StateStudyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val statesFlow = stateStudyDao.getStatesFlow()

    suspend fun save(state: StateStudy) = withContext(ioDispatcher) {
        stateStudyDao.insertOrUpdate(state)
    }

    suspend fun delete(state: StateStudy) = withContext(ioDispatcher) {
        stateStudyDao.delete(state)
    }

    suspend fun getState(id: Long) = withContext(ioDispatcher) {
        stateStudyDao.getState(id)
    }
}