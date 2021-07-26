package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.InputWorkUseCase
import com.grommade.lazymusicianship.domain.repos.RepoSection
import javax.inject.Inject

class SaveSection @Inject constructor(
    private val repoSection: RepoSection
) : InputWorkUseCase<SaveSection.Params>() {

    override suspend fun doWork(params: Params) {
        repoSection.save(params.section)
    }

    data class Params(val section: Section)
}