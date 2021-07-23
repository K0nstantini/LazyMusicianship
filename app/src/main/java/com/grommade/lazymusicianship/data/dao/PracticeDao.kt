package com.grommade.lazymusicianship.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PracticeDao : EntityDao<Practice>() {

    @Query("SELECT * FROM practice_table WHERE practice_id = :id")
    abstract suspend fun getPracticeItem(id: Long): PracticeWithPieceAndSections?

    @Query("SELECT * FROM practice_table ORDER BY date DESC, practice_id DESC")
    abstract fun getPracticesItemsFlow(): Flow<List<PracticeWithPieceAndSections>>

}