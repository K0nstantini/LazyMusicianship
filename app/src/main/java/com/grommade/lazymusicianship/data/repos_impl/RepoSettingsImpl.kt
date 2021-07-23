package com.grommade.lazymusicianship.data.repos_impl

import com.grommade.lazymusicianship.data.dao.SettingsDao
import com.grommade.lazymusicianship.domain.repos.RepoSettings
import com.grommade.lazymusicianship.util.AppCoroutineDispatchers
import javax.inject.Inject

class RepoSettingsImpl @Inject constructor(
    private val settingsDao: SettingsDao,
    private val dispatchers: AppCoroutineDispatchers
) : RepoSettings {
}