package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.ResourcesHelper
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject


class DeleteStateStudy @Inject constructor(
    private val repoStateStudy: RepoStateStudy,
    private val repoPractice: RepoPractice,
    private val resHelper: ResourcesHelper,
) : ResultUserCase<DeleteStateStudy.Params, ResultOf<Boolean>>() {


    override suspend fun doWork(params: Params): ResultOf<Boolean> {
        return when (repoPractice.countPracticesByStateId(params.state.id)) {
            0 -> {
                repoStateStudy.delete(params.state)
                ResultOf.Success(true)
            }
            else -> ResultOf.Failure(resHelper.errorStateDel)
        }
    }

    data class Params(val state: StateStudy)
}