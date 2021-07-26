package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoSection
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject


class DeleteSection @Inject constructor(
    private val repoSection: RepoSection
) : ResultUserCase<DeleteSection.Params, ResultOf<Boolean>>() {


    override suspend fun doWork(params: Params): ResultOf<Boolean> {
        return repoSection.delete(params.section)
    }

    data class Params(val section: Section)
}