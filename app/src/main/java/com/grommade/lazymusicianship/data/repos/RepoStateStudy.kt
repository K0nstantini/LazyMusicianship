package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.StateStudyDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoStateStudy @Inject constructor(
    private val stateStudyDao: StateStudyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}