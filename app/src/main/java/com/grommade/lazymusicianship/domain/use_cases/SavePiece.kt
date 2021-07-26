package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import javax.inject.Inject

class SavePiece @Inject constructor(
    private val repoPiece: RepoPiece
) : ResultUserCase<SavePiece.Params, Long>() {

    override suspend fun doWork(params: Params): Long {
        return repoPiece.save(params.piece)
    }

    data class Params(val piece: Piece)
}