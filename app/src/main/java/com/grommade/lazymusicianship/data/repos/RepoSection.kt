package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.SectionDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoSection @Inject constructor(
    private val sectionDao: SectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}