package com.grommade.lazymusicianship.data.repos_impl

import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.data.dao.StateStudyDao
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import com.grommade.lazymusicianship.util.ResourcesHelper
import com.grommade.lazymusicianship.util.ResultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoStateStudyImpl @Inject constructor(
    private val stateStudyDao: StateStudyDao,
    private val practiceDao: PracticeDao,
    private val resHelper: ResourcesHelper,
    private val dispatchers: AppCoroutineDispatchers
) : RepoStateStudy {

    override suspend fun save(state: StateStudy) = withContext(dispatchers.io) {
        stateStudyDao.insertOrUpdate(state)
    }

    override suspend fun delete(state: StateStudy): ResultOf<Boolean> = withContext(dispatchers.io) {
        return@withContext when (practiceDao.getCountPracticesByStateId(state.id)) {
            0 -> {
                stateStudyDao.delete(state)
                ResultOf.Success(true)
            }
            else -> ResultOf.Failure(resHelper.errorStateDel)
        }

    }

    override suspend fun getState(id: Long) = withContext(dispatchers.io) {
        stateStudyDao.getState(id)
    }

    override fun getStatesFlow() = stateStudyDao.getStatesFlow()
}