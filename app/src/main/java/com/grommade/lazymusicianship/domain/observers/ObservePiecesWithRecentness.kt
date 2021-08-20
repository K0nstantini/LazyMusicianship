package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.PieceWithRecentness
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePiecesWithRecentness @Inject constructor(
    private val repoPiece: RepoPiece
) : ObserveUserCase<Unit, List<PieceWithRecentness>>() {

    override fun createObservable(params: Unit): Flow<List<PieceWithRecentness>> {
        return repoPiece.getPiecesWithRecentnessFlow()
    }
}