package com.grommade.lazymusicianship.data.repos

import com.grommade.lazymusicianship.data.dao.SettingsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepoSettings @Inject constructor(
    private val settingsDao: SettingsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
}