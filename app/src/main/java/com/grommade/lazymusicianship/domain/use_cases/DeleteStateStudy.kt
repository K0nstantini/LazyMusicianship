package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import javax.inject.Inject


class DeleteStateStudy @Inject constructor(
    private val repoStateStudy: RepoStateStudy,
    private val repoPractice: RepoPractice,
) : ResultUserCase<DeleteStateStudy.Params, Boolean>() {


    override suspend fun doWork(params: Params): Boolean {
        return when (repoPractice.countPracticesByStateId(params.state.id)) {
            0 -> {
                repoStateStudy.delete(params.state)
                true
            }
            else -> false
        }
    }

    data class Params(val state: StateStudy)
}