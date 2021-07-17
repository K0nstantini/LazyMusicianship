package com.grommade.lazymusicianship.data.repos

import androidx.room.Transaction
import com.grommade.lazymusicianship.data.dao.DataTransferDao
import com.grommade.lazymusicianship.data.entity.DataTransfer
import com.grommade.lazymusicianship.data.entity.Section
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoDataTransfer @Inject constructor(
    private val dataTransferDao: DataTransferDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun save(dataTransfer: DataTransfer) = withContext(ioDispatcher) {
        dataTransferDao.insert(dataTransfer)
    }

    @Transaction
    suspend fun getData(): DataTransfer? = withContext(ioDispatcher) {
        val dataTransfer = dataTransferDao.getData()
        dataTransferDao.deleteAll()
        return@withContext dataTransfer
    }

}