package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.PracticeWithDetails
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePracticesWithDetails @Inject constructor(
    private val repoPractice: RepoPractice
) : ObserveUserCase<Unit, List<PracticeWithDetails>>() {

    override fun createObservable(params: Unit): Flow<List<PracticeWithDetails>> {
        return repoPractice.practiceWithDetailsFlow()
    }
}