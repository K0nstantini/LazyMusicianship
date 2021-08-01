package com.grommade.lazymusicianship.data.repos_impl

import com.grommade.lazymusicianship.data.dao.PracticeDao
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class RepoPracticeImpl @Inject constructor(
    private val practiceDao: PracticeDao,
    private val dispatchers: AppCoroutineDispatchers
) : RepoPractice {

    override fun getPracticesItemsFlow() =
        practiceDao.getPracticesItemsFlow()

    override fun getTimesByDaysFlow(startDate: LocalDate, endDate: LocalDate) =
        practiceDao.getTimesByDaysFlow(startDate, endDate)

    override suspend fun save(practice: Practice) = withContext(dispatchers.io) {
        practiceDao.insertOrUpdate(practice)
    }

    override suspend fun delete(practice: Practice) = withContext(dispatchers.io) {
        practiceDao.delete(practice)
    }

    override suspend fun getPracticeItem(id: Long) = withContext(dispatchers.io) {
        practiceDao.getPracticeItem(id)
    }
}