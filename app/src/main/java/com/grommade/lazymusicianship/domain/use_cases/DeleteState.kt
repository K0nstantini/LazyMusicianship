package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject


class DeleteState @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : ResultUserCase<DeleteState.Params, ResultOf<Boolean>>() {

    override suspend fun doWork(params: Params): ResultOf<Boolean> {
        return repoStateStudy.delete(params.state)
    }

    data class Params(val state: StateStudy)
}