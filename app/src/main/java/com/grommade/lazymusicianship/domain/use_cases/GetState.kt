package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject

class GetState @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : ResultUserCase<GetState.Params, ResultOf<StateStudy>>() {

    override suspend fun doWork(params: Params): ResultOf<StateStudy> {
        return when (val state = repoStateStudy.getState(params.stateId)) {
            is StateStudy -> ResultOf.Success(state)
            else -> ResultOf.Failure()
        }
    }

    data class Params(val stateId: Long)
}