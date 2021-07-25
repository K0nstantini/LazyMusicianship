package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import javax.inject.Inject

class GetState @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : ResultUserCase<GetState.Params, StateStudy?>() {

    override suspend fun doWork(params: Params): StateStudy? {
        return repoStateStudy.getState(params.stateId)
    }

    data class Params(val stateId: Long)
}