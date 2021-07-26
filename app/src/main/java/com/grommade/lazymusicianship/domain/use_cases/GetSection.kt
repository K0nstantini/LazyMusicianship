package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoSection
import com.grommade.lazymusicianship.util.ResultOf
import javax.inject.Inject

class GetSection @Inject constructor(
    private val repoSection: RepoSection
) : ResultUserCase<GetSection.Params, ResultOf<Section>>() {

    override suspend fun doWork(params: Params): ResultOf<Section> {
        return when (val section = repoSection.getSection(params.sectionId)) {
            is Section -> ResultOf.Success(section)
            else -> ResultOf.Failure()
        }
    }

    data class Params(val sectionId: Long)
}