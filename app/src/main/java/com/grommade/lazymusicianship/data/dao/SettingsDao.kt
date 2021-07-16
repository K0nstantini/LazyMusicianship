package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Settings

@Dao
abstract class SettingsDao: EntityDao<Settings>() {

    @Query("SELECT COUNT(*) FROM settings_table")
    abstract suspend fun getCount(): Int
}