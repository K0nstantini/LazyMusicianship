package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePieces @Inject constructor(
    private val repoPiece: RepoPiece
) : ObserveUserCase<Unit, List<Piece>>() {

    override fun createObservable(params: Unit): Flow<List<Piece>> {
        return repoPiece.getPiecesFlow()
    }
}