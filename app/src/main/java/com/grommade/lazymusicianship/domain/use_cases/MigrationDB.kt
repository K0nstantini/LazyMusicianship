package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.domain.InputWorkUseCase
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MigrationDB @Inject constructor(
    private val repoPractice: RepoPractice
) : InputWorkUseCase<Unit>() {

    override suspend fun doWork(params: Unit) {

        val practices = repoPractice.practicesWithDetailsFlow().first().filter { it.sectionFrom != null }
        practices.forEach { practiceWithDetails ->
            val practice = practiceWithDetails.practice
            val sections = practiceWithDetails.pieceWithSections.sections
            val sectionFrom = practiceWithDetails.sectionFrom ?: return@forEach
            val sectionTo = practiceWithDetails.sectionTo ?: return@forEach
            practice.copy(sections = sections.getMidSections(sectionFrom, sectionTo)).save()
        }
    }

    private fun List<Section>.getMidSections(
        sectionFrom: Section,
        sectionTo: Section
    ): List<Long> {
        val parent = sectionFrom.parentId
        return filter { it.parentId == parent && it.order in sectionFrom.order..sectionTo.order }.map { it.id }
    }

    private suspend fun Practice.save() = repoPractice.save(this)
}