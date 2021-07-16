package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.StateStudySectionDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoStateStudySection@Inject constructor(
    private val stateStudySectionDao: StateStudySectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}