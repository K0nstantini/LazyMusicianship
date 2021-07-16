package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.StateStudySection

@Dao
abstract class StateStudySectionDao : EntityDao<StateStudySection>() {
    @Query("SELECT COUNT(*) FROM state_study_section_table")
    abstract suspend fun getCount(): Int
}