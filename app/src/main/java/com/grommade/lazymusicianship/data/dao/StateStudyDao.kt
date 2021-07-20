package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.StateStudy

@Dao
abstract class StateStudyDao: EntityDao<StateStudy>() {
}