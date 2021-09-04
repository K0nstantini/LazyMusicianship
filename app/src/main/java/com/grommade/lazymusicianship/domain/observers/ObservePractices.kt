package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePractices @Inject constructor(
    private val repoPractice: RepoPractice
) : ObserveUserCase<Unit, List<PracticeWithPieceAndSections>>() {

    override fun createObservable(params: Unit): Flow<List<PracticeWithPieceAndSections>> {
        return repoPractice.practicesItemsFlow()
    }
}