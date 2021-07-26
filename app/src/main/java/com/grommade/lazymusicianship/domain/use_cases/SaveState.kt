package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.InputWorkUseCase
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import javax.inject.Inject

class SaveState @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : InputWorkUseCase<SaveState.Params>() {

    override suspend fun doWork(params: Params) {
        repoStateStudy.save(params.state)
    }

    data class Params(val state: StateStudy)
}