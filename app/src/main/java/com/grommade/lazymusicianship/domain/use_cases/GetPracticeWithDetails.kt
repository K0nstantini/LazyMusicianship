package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.PracticeWithDetails
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject

class GetPracticeWithDetails @Inject constructor(
    private val repoPractice: RepoPractice
) : ResultUserCase<GetPracticeWithDetails.Params, ResultOf<PracticeWithDetails>>() {

    override suspend fun doWork(params: Params): ResultOf<PracticeWithDetails> {
        return when (val practice = repoPractice.practiceWithDetails(params.practiceId)) {
            is PracticeWithDetails -> ResultOf.Success(practice)
            else -> ResultOf.Failure()
        }
    }

    data class Params(val practiceId: Long)
}