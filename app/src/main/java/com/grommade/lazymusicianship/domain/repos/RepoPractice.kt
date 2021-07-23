package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import kotlinx.coroutines.flow.Flow

interface RepoPractice {
    suspend fun save(practice: Practice): Long
    suspend fun delete(practice: Practice)
    suspend fun getPracticeItem(id: Long): PracticeWithPieceAndSections?
    fun getPracticesItemsFlow(): Flow<List<PracticeWithPieceAndSections>>
}