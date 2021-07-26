package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject

class GetPractice @Inject constructor(
    private val repoPractice: RepoPractice
) : ResultUserCase<GetPractice.Params, ResultOf<PracticeWithPieceAndSections>>() {

    override suspend fun doWork(params: Params): ResultOf<PracticeWithPieceAndSections> {
        return when (val practice = repoPractice.getPracticeItem(params.practiceId)) {
            is PracticeWithPieceAndSections -> ResultOf.Success(practice)
            else -> ResultOf.Failure()
        }
    }

    data class Params(val practiceId: Long)
}