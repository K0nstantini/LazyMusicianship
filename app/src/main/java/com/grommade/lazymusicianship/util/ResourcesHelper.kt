package com.grommade.lazymusicianship.util

import android.content.Context
import com.grommade.lazymusicianship.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val errorStateDel
        get() = context.getString(R.string.snack_state_already_in_use)
}