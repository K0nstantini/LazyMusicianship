package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import javax.inject.Inject

class GetStates @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : ResultUserCase<Unit, List<StateStudy>>() {

    override suspend fun doWork(params: Unit): List<StateStudy> {
        return repoStateStudy.getAllStates()
    }

}