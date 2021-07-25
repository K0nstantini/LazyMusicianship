package com.grommade.lazymusicianship.domain.observers

import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.ObserveUserCase
import com.grommade.lazymusicianship.domain.repos.RepoSection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSections @Inject constructor(
    private val repoSection: RepoSection
) : ObserveUserCase<ObserveSections.Params, List<Section>>() {

    override fun createObservable(params: Params): Flow<List<Section>> {
        return repoSection.getSectionsFlow(params.pieceId)
    }

    data class Params(val pieceId: Long)
}