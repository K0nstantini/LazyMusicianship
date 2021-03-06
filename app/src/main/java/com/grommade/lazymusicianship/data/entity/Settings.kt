package com.grommade.lazymusicianship.data.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "settings_table")
data class Settings(
    @PrimaryKey
    override val id: Long = 1,
) : AppEntity {
}