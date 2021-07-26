package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.domain.repos.RepoSection
import javax.inject.Inject

class NewSection @Inject constructor(
    private val repoSection: RepoSection
) : ResultUserCase<NewSection.Params, Section>() {

    override suspend fun doWork(params: Params): Section {
        val lastSection = repoSection.getLastCreated(params.pieceId) ?: Section()
        return Section(
            pieceId = params.pieceId,
            parentId = params.parentId,
            tempo = lastSection.tempo,
            firstTime = lastSection.firstTime
        )
    }

    data class Params(val pieceId: Long, val parentId: Long)
}