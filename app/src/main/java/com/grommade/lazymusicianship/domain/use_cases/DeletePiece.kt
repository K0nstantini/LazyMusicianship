package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject


class DeletePiece @Inject constructor(
    private val repoPiece: RepoPiece
) : ResultUserCase<DeletePiece.Params, ResultOf<Boolean>>() {

    override suspend fun doWork(params: Params): ResultOf<Boolean> {
        return repoPiece.delete(params.piece)

    }

    data class Params(val piece: Piece)
}