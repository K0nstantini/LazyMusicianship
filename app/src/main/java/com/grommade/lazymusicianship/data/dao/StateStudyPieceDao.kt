package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.StateStudyPiece

@Dao
abstract class StateStudyPieceDao: EntityDao<StateStudyPiece>() {
    @Query("SELECT COUNT(*) FROM state_study_piece_table")
    abstract suspend fun getCount(): Int
}