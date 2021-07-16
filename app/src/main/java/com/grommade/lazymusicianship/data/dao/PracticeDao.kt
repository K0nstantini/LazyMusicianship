package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import com.grommade.lazymusicianship.data.entity.Practice

@Dao
abstract class PracticeDao : EntityDao<Practice>() {
}