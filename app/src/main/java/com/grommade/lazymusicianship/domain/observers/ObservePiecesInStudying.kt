package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObservePiecesInStudying @Inject constructor(
    private val repoPractice: RepoPractice
) : ObserveUserCase<Unit, List<Piece>>() {

    override fun createObservable(params: Unit): Flow<List<Piece>> {
        return repoPractice.getPracticesItemsFlow()
            .map { practices ->
                practices.map { it.piece }.distinct()
            }
    }
}