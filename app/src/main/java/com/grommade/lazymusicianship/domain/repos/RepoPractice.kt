package com.grommade.lazymusicianship.domain.repos

import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithDetails
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface RepoPractice {
    suspend fun save(practice: Practice): Long
    suspend fun delete(practice: Practice)
    suspend fun deleteAll()
    suspend fun practiceWithDetails(id: Long): PracticeWithDetails?
    fun practicesWithDetailsFlow(): Flow<List<PracticeWithDetails>>
    fun getTimesByDaysFlow(startDate: LocalDate, endDate: LocalDate): Flow<List<PracticeDao.TimesByDays>>
}