package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.entity.StateStudy
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StateStudyDao: EntityDao<StateStudy>() {

    @Query("DELETE FROM state_study_table  WHERE id = :id")
    abstract suspend fun delete(id: Long)

    @Query("SELECT * FROM state_study_table WHERE id = :id")
    abstract suspend fun getState(id: Long): StateStudy?

    @Query("SELECT * FROM state_study_table ORDER BY name")
    abstract fun getStatesFlow(): Flow<List<StateStudy>>
}