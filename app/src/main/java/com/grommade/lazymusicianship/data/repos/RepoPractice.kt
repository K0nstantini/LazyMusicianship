package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.PracticeDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoPractice @Inject constructor(
    private val practiceDao: PracticeDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}