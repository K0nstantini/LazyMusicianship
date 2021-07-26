package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.domain.InputWorkUseCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import javax.inject.Inject

class SavePractice @Inject constructor(
    private val repoPractice: RepoPractice
) : InputWorkUseCase<SavePractice.Params>() {

    override suspend fun doWork(params: Params) {
        repoPractice.save(params.practice)
    }

    data class Params(val practice: Practice)
}