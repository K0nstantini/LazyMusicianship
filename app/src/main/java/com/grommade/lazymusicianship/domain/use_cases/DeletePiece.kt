package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.util.ResourcesHelper
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject


class DeletePiece @Inject constructor(
    private val repoPiece: RepoPiece,
    private val repoPractice: RepoPractice,
    private val resHelper: ResourcesHelper,
) : ResultUserCase<DeletePiece.Params, ResultOf<Boolean>>() {


    override suspend fun doWork(params: Params): ResultOf<Boolean> {
        return when (repoPractice.countPracticesByPieceId(params.piece.id)) {
            0 -> {
                repoPiece.delete(params.piece)
                ResultOf.Success(true)
            }
            else -> ResultOf.Failure(resHelper.errorPieceDel)
        }
    }

    data class Params(val piece: Piece)
}