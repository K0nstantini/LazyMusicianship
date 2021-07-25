package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject

class GetPiece @Inject constructor(
    private val repoPiece: RepoPiece
) : ResultUserCase<GetPiece.Params, ResultOf<Piece>>() {

    override suspend fun doWork(params: Params): ResultOf<Piece> {
        return when (val state = repoPiece.getPiece(params.pieceId)) {
            is Piece -> ResultOf.Success(state)
            else -> ResultOf.Failure()
        }
    }

    data class Params(val pieceId: Long)
}