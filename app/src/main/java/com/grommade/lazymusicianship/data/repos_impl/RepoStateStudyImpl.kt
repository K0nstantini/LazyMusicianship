package com.grommade.lazymusicianship.data.repos_impl

import com.grommade.lazymusicianship.data.dao.StateStudyDao
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoStateStudyImpl @Inject constructor(
    private val stateStudyDao: StateStudyDao,
    private val dispatchers: AppCoroutineDispatchers
): RepoStateStudy {

    override suspend fun save(state: StateStudy) = withContext(dispatchers.io) {
        stateStudyDao.insertOrUpdate(state)
    }

    override suspend fun delete(state: StateStudy) = withContext(dispatchers.io) {
        stateStudyDao.delete(state)
    }

    override suspend fun getState(id: Long) = withContext(dispatchers.io) {
        stateStudyDao.getState(id)
    }

    override fun getStatesFlow() = stateStudyDao.getStatesFlow()
}