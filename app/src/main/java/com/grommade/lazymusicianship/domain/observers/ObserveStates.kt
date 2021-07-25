package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStates @Inject constructor(
    private val repoStateStudy: RepoStateStudy
) : ObserveUserCase<Unit, List<StateStudy>>() {

    override fun createObservable(params: Unit): Flow<List<StateStudy>> {
        return repoStateStudy.getStatesFlow()
    }
}